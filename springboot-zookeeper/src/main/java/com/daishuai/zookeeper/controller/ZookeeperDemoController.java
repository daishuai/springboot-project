package com.daishuai.zookeeper.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Daishuai
 * @date 2020/10/19 15:42
 */
@Slf4j
@RestController
public class ZookeeperDemoController {

    @Autowired
    private CuratorFramework zookeeperClient;

    @GetMapping(value = "/demo")
    public Object demo() throws Exception {
        NodeCache nodeCache = new NodeCache(zookeeperClient, "/demo");
        nodeCache.start();
        nodeCache.getListenable().addListener(() -> {
            System.out.println("node change");
        });

        PathChildrenCache pathChildrenCache = new PathChildrenCache(zookeeperClient, "/", true);
        pathChildrenCache.start();
        pathChildrenCache.getListenable().addListener((client, event) -> {
            PathChildrenCacheEvent.Type type = event.getType();
            ChildData data = event.getData();
            String path = data.getPath();
            if (type == PathChildrenCacheEvent.Type.CHILD_REMOVED && StringUtils.equals(path, "/demo")) {
                log.info("子节点被删除: {}", "/demo");
            }
        });

        //check the node exist
        Stat stat1 = zookeeperClient.checkExists().forPath("/demo");
        if (stat1 != null) {
            // delete node
            zookeeperClient.delete().forPath("/demo");
            // 指定版本，递归删除子节点
            //zookeeperClient.delete().guaranteed().deletingChildrenIfNeeded().withVersion(100).forPath("/demo");
        }
        // 创建节点，默认为持久化节点
        String result = zookeeperClient.create().forPath("/demo", "Hello World".getBytes());
        log.info("zookeeper create permanent node with data, result: {}", result);

        //zookeeperClient.create().withMode(CreateMode.EPHEMERAL).forPath("/test");

        // 更新节点数据
        zookeeperClient.setData().forPath("/demo", "Java In Action".getBytes());

        Stat stat = new Stat();
        // 获取节点数据
        byte[] bytes = zookeeperClient.getData().storingStatIn(stat).forPath("/demo");
        log.info("zookeeper get data from path: {}, the data is: {}", "/demo", new String(bytes));

        List<String> paths = zookeeperClient.getChildren().forPath("/");
        log.info("nodes: {}", JSON.toJSONString(paths));


        return 200;
    }
}
