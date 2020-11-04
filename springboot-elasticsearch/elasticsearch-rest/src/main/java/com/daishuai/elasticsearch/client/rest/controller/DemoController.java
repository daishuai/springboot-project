package com.daishuai.elasticsearch.client.rest.controller;

import com.alibaba.fastjson.JSON;
import com.daishuai.elasticsearch.client.rest.config.ElasticSearchClientBuilder;
import com.daishuai.elasticsearch.client.rest.service.ElasticSearchApi;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * @author Daishuai
 * @date 2020/10/25 11:47
 */
@Slf4j
@RestController
public class DemoController {

    @Autowired
    private ElasticSearchApi elasticSearchApi;

    @Autowired
    private ElasticSearchClientBuilder builder;

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
        ElasticSearchApi devApi = builder.builderApi("default");
        BoolQueryBuilder boolQuery = boolQuery();
        SearchSourceBuilder source = new SearchSourceBuilder().query(boolQuery).size(1000).explain(false);
        SearchResponse searchResponse = elasticSearchApi.search("a_fire_yjzs", "yjzs", source);
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            log.info("index data: {}", hit.getSourceAsString());
            devApi.index(hit.getIndex(), hit.getType(), hit.getId(), hit.getSourceAsMap());
        }
        return 200;
    }

    public static void main(String[] args) {
        BoolQueryBuilder should = boolQuery().should(termsQuery("DWXS.ID", "01", "02", "03", "04", "05", "06", "07", "08"))
                .should(boolQuery().must(termQuery("DWXS.ID", "09")).must(termsQuery("DWJB.ID", "01", "02", "03")));
        should.minimumShouldMatch(1);
        System.out.println(should.toString());
    }
}
