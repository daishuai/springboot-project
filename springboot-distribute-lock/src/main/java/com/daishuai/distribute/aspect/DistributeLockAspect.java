package com.daishuai.distribute.aspect;

import com.daishuai.distribute.lock.DistributeLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;

/**
 * @author Daishuai
 * @date 2020/9/21 18:03
 */
@Aspect
public class DistributeLockAspect {

    @Autowired
    private DistributeLock distributeLock;

    @Pointcut(value = "@annotation(com.daishuai.distribute.annotation.DistributeLock)")
    public void pointCut(){}

    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        com.daishuai.distribute.annotation.DistributeLock annotation = method.getAnnotation(com.daishuai.distribute.annotation.DistributeLock.class);
        return null;
    }
}
