package com.daishuai.elasticsearch.client.rest.controller;

import com.daishuai.elasticsearch.client.rest.service.ElasticSearchApi;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.*;

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

        BoolQueryBuilder should = boolQuery().should(termsQuery("DWXS.ID", "01", "02", "03", "04", "05", "06", "07", "08"))
                .should(boolQuery().must(termQuery("DWXS.ID", "09")).must(termsQuery("DWJB.ID", "01", "02", "03")));
        System.out.println(should.toString());
        return elasticSearchApi.get("a_fire_zqxx", "zqxx", "bf726445193d3c76b16a0ec125fe0503", "ZQBH", "SZDXFJG");
    }

    public static void main(String[] args) {
        BoolQueryBuilder should = boolQuery().should(termsQuery("DWXS.ID", "01", "02", "03", "04", "05", "06", "07", "08"))
                .should(boolQuery().must(termQuery("DWXS.ID", "09")).must(termsQuery("DWJB.ID", "01", "02", "03")));
        should.minimumShouldMatch(1);
        System.out.println(should.toString());
    }
}
