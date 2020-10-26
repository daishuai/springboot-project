package com.daishuai.elasticsearch.client.rest.controller;

import com.daishuai.elasticsearch.client.rest.service.ElasticSearchApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daishuai
 * @date 2020/10/25 11:47
 */
@RestController
public class DemoController {

    @Autowired
    private ElasticSearchApi elasticSearchApi;

    @GetMapping(value = "/indexDemo")
    public Object indexDemo() {
        Map<String, Object> data = new HashMap<>(8);
        data.put("GXRY", "张三");
        data.put("LYBH", "123456");
        data.put("RKRY", "江苏省苏州市");
        return elasticSearchApi.index("a_fire_zqly", "zqly", "123456", data);
    }

    @GetMapping(value = "/getDemo")
    public Object getDemo() {
        return elasticSearchApi.get("a_fire_zqxx", "zqxx", "bf726445193d3c76b16a0ec125fe0503", "ZQBH", "SZDXFJG");
    }
}
