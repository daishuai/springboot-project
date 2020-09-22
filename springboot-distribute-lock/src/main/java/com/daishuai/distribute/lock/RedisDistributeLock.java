package com.daishuai.distribute.lock;

import io.lettuce.core.RedisAsyncCommandsImpl;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Redis缓存实现分布式锁
 *
 * @author Daishuai
 * @date 2020/9/21 11:20
 */
@Slf4j
@Component
public class RedisDistributeLock implements DistributeLock{

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    private final ThreadLocal<String> lockFlag = new ThreadLocal<String>();

    private static final long EXPIRE_TIME = 10000L;

    private static final String RELEASE_LOCK_LUA;

    static {
        //Lua 脚本
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append(" return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append(" return 0 ");
        sb.append("end ");
        RELEASE_LOCK_LUA = sb.toString();
    }

    @Override
    public boolean lock(String lockKey) {
        return this.tryLock(lockKey, EXPIRE_TIME);
    }

    @Override
    public boolean lock(String lockKey, long expireMillis, int retryTimes, long retryInterval) {
        boolean result = this.tryLock(lockKey, expireMillis);
        //如果获取锁失败，按照传入的重试次数进行重试
        while (!result && retryTimes-- > 0) {
            try {
                log.info("acquire lock failed, retrying...{}", retryTimes);
                Thread.sleep(retryInterval);
            } catch (InterruptedException e) {
                return false;
            }
            result = this.tryLock(lockKey, expireMillis);
        }
        return result;
    }

    /**
     * Redis分布式锁存在的问题
     * ----将锁资源放入Redis，setIfAbsent(key, value)
     * ----设置过期时间，expire(key, expire, timeUnit)
     * ----释放锁，delete(key)
     * 1、在调用setIfAbsent()方法后线程挂掉了，即没有给锁定的资源设置过期时间，默认是永不过期，那么这个锁就会一直存在，所以需要保证设置锁及其过期时间两个操作的原子性
     *   解决方法：jedis当中有这种原子操作的方法，通过RedisTemplate的execute方法获取到jedis里操作命令的对象，JedisCommands.set(key,value,"NX","PX",expire)
     *   NX:表示只用当前锁定资源不存在的时候才能SET成功。
     *   PX:expire表示锁定的资源的自动过期时间，单位是毫秒。
     * 2、线程T1获取锁，线程T1执行业务操作，由于某些原因阻塞了较长时间，锁自动过期，即锁自动释放了，线程T2获取锁，线程T1业务操作完成，释放锁（其实是释放的线程T2的锁）
     * 3、线程T1执行业务时间太长超过锁过期时间，锁自动过期，线程T2获取锁，同时有两个线程执行业务
     */

    @Override
    public boolean releaseLock(String lockKey) {
        //释放锁的时候，有可能因为持有锁之后方法执行时间大于锁的有效期，此时有可能已经被另外一个线程持有锁，所以不能直接删除
        String value = lockFlag.get();
        try {
            //使用lua脚本删除redis中匹配的value的key，可以避免由于方法执行时间过长而redis锁自动过期失效的时候误删其他线程的锁
            //Spring自带的执行脚本方法中，集群模式直接抛出不支持执行脚本的异常，所以只能拿到原redis的connection来执行脚本
            Long result = redisTemplate.execute((RedisCallback<Long>) connection -> {
                Object nativeConnection = connection.getNativeConnection();
                RedisSerializer keySerializer = redisTemplate.getKeySerializer();
                RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
                byte[] keyBytes = keySerializer.serialize(lockKey);
                byte[] valeBytes = valueSerializer.serialize(value);
                Object[] keys = {keyBytes};
                // Lettuce连接包下 Redis 单机模式
                if (nativeConnection instanceof RedisAsyncCommands) {
                    RedisAsyncCommands redisAsyncCommands = (RedisAsyncCommands) nativeConnection;
                    RedisCommands redisCommands = redisAsyncCommands.getStatefulConnection().sync();
                    return (Long) redisCommands.eval(RELEASE_LOCK_LUA, ScriptOutputType.INTEGER, keys, valeBytes);
                }

                // Lettuce连接包下 Redis 集群模式
                if (nativeConnection instanceof RedisAdvancedClusterAsyncCommands) {
                    RedisAdvancedClusterAsyncCommands redisAdvancedClusterAsyncCommands = (RedisAdvancedClusterAsyncCommands) nativeConnection;
                    RedisAdvancedClusterCommands clusterCommands = redisAdvancedClusterAsyncCommands.getStatefulConnection().sync();
                    return (Long) clusterCommands.eval(RELEASE_LOCK_LUA, ScriptOutputType.INTEGER, keys, valeBytes);
                }

                //集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能翻开执行
                //集群模式
                /*if (nativeConnection instanceof JedisCluster) {
                    return (Long) ((JedisCluster) nativeConnection).eval(UNLOCK_LUA, keys, value);
                }*/
                //单机模式
                /*if (nativeConnection instanceof Jedis) {
                    return (Long) ((Jedis) nativeConnection).eval(UNLOCK_LUA, keys, value);
                }*/
                return 0L;
            });
            if (result != null && result > 0) {
                log.info("success release lock ...");
                lockFlag.remove();
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("release lock occurred exception :{}", e.getMessage(), e);
        }
        return false;
    }

    private boolean tryLock(String lockKey, long expireMillis) {
        String value = UUID.randomUUID().toString().replaceAll("-", "");
        try {
            String result = redisTemplate.execute((RedisCallback<String>) connection -> {
                Object nativeConnection = connection.getNativeConnection();
                RedisSerializer keySerializer = redisTemplate.getKeySerializer();
                RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
                byte[] keyBytes = keySerializer.serialize(lockKey);
                byte[] valueBytes = valueSerializer.serialize(value);
                SetArgs setArgs = SetArgs.Builder.nx().px(expireMillis);
                // lettuce连接包下 redis 单机模式setnx
                if (nativeConnection instanceof RedisAsyncCommands) {
                    RedisAsyncCommandsImpl redisAsyncCommands = (RedisAsyncCommandsImpl) nativeConnection;
                    RedisCommands redisCommands = redisAsyncCommands.getStatefulConnection().sync();
                    try {
                        return redisCommands.set(keyBytes, valueBytes, setArgs);
                    } catch (Exception e) {
                        log.error("获取锁失败: {}", e.getMessage(), e);
                    }
                }
                // lettuce连接包下 redis 集群模式setnx
                if (nativeConnection instanceof RedisAdvancedClusterAsyncCommands) {
                    RedisAdvancedClusterAsyncCommands redisAdvancedClusterAsyncCommands = (RedisAdvancedClusterAsyncCommands) nativeConnection;
                    RedisAdvancedClusterCommands clusterCommands = redisAdvancedClusterAsyncCommands.getStatefulConnection().sync();
                    clusterCommands.set(keyBytes, valueBytes, setArgs);
                }

                //集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能翻开执行
                // Jedis连接包下，Redis集群模式
                /*if (nativeConnection instanceof JedisCluster) {
                    return (Long) ((JedisCluster) nativeConnection).eval(UNLOCK_LUA, keys, args);
                }*/
                // Jedis连接包下，Redis单机模式
                /*if (nativeConnection instanceof Jedis) {
                    return (Long) ((Jedis) nativeConnection).eval(UNLOCK_LUA, keys, args);
                }*/

                return "Failed";
            });
            if (StringUtils.equals("OK", result)) {
                log.info("success acquire lock ...");
                lockFlag.set(value);
            }
            return StringUtils.equals("OK", result);
        } catch (Exception e) {
            log.error("set redis occurred an exception:{}", e.getMessage(), e);
            return false;
        }
    }
}
