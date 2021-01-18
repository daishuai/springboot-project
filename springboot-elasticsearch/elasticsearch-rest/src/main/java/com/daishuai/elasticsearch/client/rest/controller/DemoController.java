package com.daishuai.elasticsearch.client.rest.controller;

import com.alibaba.fastjson.JSON;
import com.daishuai.elasticsearch.client.rest.config.ElasticSearchClientBuilder;
import com.daishuai.elasticsearch.client.rest.cutomer.CustomTotalHits;
import com.daishuai.elasticsearch.client.rest.service.RestElasticSearchApi;
import com.kem.elastic.plugin.CircleQueryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.search.CustomSearchResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.geo.ShapeRelation;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoShapeQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * @author Daishuai
 * @date 2020/10/25 11:47
 */
@Slf4j
@RestController
public class DemoController implements InitializingBean {

    private RestElasticSearchApi elasticSearchApi7x;
    private RestElasticSearchApi elasticSearchApi6x;
    private RestElasticSearchApi elasticSearchApi5x;

    @Autowired
    private ElasticSearchClientBuilder builder;

    @GetMapping(value = "/search")
    public Object search() {
        //this.syncData("a_fire_xfdw", "xfdw");
        BoolQueryBuilder boolQuery = boolQuery();
        boolQuery.mustNot(termQuery("JLZT", "0"));
        SearchSourceBuilder source = new SearchSourceBuilder().query(boolQuery);
        //source.trackTotalHits(true);
        source.from(10).size(100);
        source.explain(false);
        System.out.println(source.toString());
        CustomSearchResponse response5x = elasticSearchApi5x.searchData("a_fire_xfdw", "xfdw", source);
        CustomSearchResponse response6x = elasticSearchApi6x.searchData("a_fire_xfdw", "xfdw", source);
        CustomSearchResponse response7x = elasticSearchApi7x.searchData("a_fire_xfdw", "xfdw", source);
        //CustomTotalHits customTotalHits = response7x.getHits().getCustomTotalHits();
        //log.info("ES 5.X Response: {}", JSON.toJSONString(response5x));
        //log.info("ES 6.X Response: {}", JSON.toJSONString(response6x));
        //log.info("ES 7.X Response: {}", JSON.toJSONString(response7x));
        return null;
    }

    @GetMapping(value = "/geoShapeQuery")
    public Object geoShapeQuery() throws IOException {
        BoolQueryBuilder boolQuery = boolQuery();
        GeoShapeQueryBuilder geoShapeQuery = QueryBuilders.geoShapeQuery("SHAPE", CircleQueryBuilder.newCircleBuilder()
                .center(131.11, 31.1)
                .radius(10000, DistanceUnit.KILOMETERS).build())
                .relation(ShapeRelation.WITHIN);
        boolQuery.must(geoShapeQuery);
        SearchSourceBuilder source = new SearchSourceBuilder().query(boolQuery);
        CustomSearchResponse response6x = elasticSearchApi6x.searchData("a_fire_xfdw", "xfdw", source);
        CustomSearchResponse response7x = elasticSearchApi7x.searchData("a_fire_xfdw", "xfdw", source);
        log.info("ES 6.X Response: {}", JSON.toJSONString(response6x));
        log.info("ES 7.X Response: {}", JSON.toJSONString(response7x));
        return null;
    }

    @GetMapping(value = "/update")
    public Object update() {
        BoolQueryBuilder boolQuery = boolQuery().mustNot(termQuery("JLZT", "0"))
                .must(termQuery("DWBH", "360c5e27c4614f46926651251cbc57ce"));
        SearchSourceBuilder searchSource = new SearchSourceBuilder().query(boolQuery);
        CustomSearchResponse response6x = elasticSearchApi6x.searchData("a_fire_xfdw", "xfdw", searchSource);
        CustomSearchResponse response7x = elasticSearchApi7x.searchData("a_fire_xfdw", "xfdw", searchSource);
        SearchHit hit6x = response6x.getHits().getHits()[0];
        SearchHit hit7x = response7x.getHits().getHits()[0];
        Map<String, Object> sourceAsMap6x = hit6x.getSourceAsMap();
        Map<String, Object> sourceAsMap7x = hit7x.getSourceAsMap();
        System.out.println(hit6x.getSourceAsString());
        elasticSearchApi5x.updateByIdImmediate("a_fire_xfdw", "xfdw", hit6x.getId(), sourceAsMap6x);
        //elasticSearchApi6x.updateByIdImmediate("a_fire_xfdw", "xfdw", hit6x.getId(), sourceAsMap6x);
        //elasticSearchApi7x.updateByIdImmediate("a_fire_xfdw", "xfdw", hit7x.getId(), sourceAsMap7x);

        return null;
    }

    @GetMapping(value = "/delete")
    public Object delete() {
        //DeleteResponse deleteResponse6x = elasticSearchApi6x.deleteById("a_fire_xfdw", "xfdw", "360c5e27c4614f46926651251cbc57ce");
        DeleteResponse deleteResponse7x = elasticSearchApi7x.deleteById("a_fire_xfdw", "xfdw", "360c5e27c4614f46926651251cbc57ce");
        return null;
    }

    @GetMapping(value = "/batch")
    public Object batch() {
        BoolQueryBuilder boolQuery = boolQuery();
        SearchSourceBuilder searchSource = new SearchSourceBuilder().query(boolQuery).size(1000);
        CustomSearchResponse searchResponse6x = elasticSearchApi6x.searchData("a_fire_xfdw", "xfdw", searchSource);
        SearchHit[] hits = searchResponse6x.getHits().getHits();
        for (SearchHit hit : hits) {
            elasticSearchApi7x.updateToProcessor("a_fire_xfdw", "xfdw", hit.getId(), hit.getSourceAsString());
        }
        return null;
    }

    private void syncData(String index, String type) {
        BoolQueryBuilder boolQuery = boolQuery();
        CustomSearchResponse response = elasticSearchApi6x.searchScrollData(index, type, boolQuery, 2000, new Scroll(TimeValue.timeValueMinutes(3L)));
        if (response == null) {
            return;
        }
        long copyCount = 0;
        SearchHit[] hits = response.getHits().getHits();
        if (ArrayUtils.isEmpty(hits)) {
            return;
        }
        for (SearchHit hit : hits) {
            elasticSearchApi7x.upsertToProcessor(index, type, hit.getId(), hit.getSourceAsString());
        }
        copyCount += hits.length;
        String scrollId = response.getScrollId();
        while (true) {
            try {
                SearchResponse scrollData = elasticSearchApi6x.searchScroll(scrollId, new Scroll(TimeValue.timeValueMinutes(3L)));
                if (scrollData == null) {
                    break;
                }
                scrollId = scrollData.getScrollId();
                SearchHit[] dataScroll = scrollData.getHits().getHits();
                if (dataScroll.length == 0) {
                    log.info("滚动结束");
                    log.info("拷贝滚动数据总条数=={}", response.getHits().getTotalHits());
                    log.info("当前【{}】索引拷贝滚动数据条数=={}", index, copyCount);
                    break;
                }
                for (SearchHit searchHit : dataScroll) {
                    elasticSearchApi7x.upsertToProcessor(index, type, searchHit.getId(), searchHit.getSourceAsString());
                }
                copyCount += dataScroll.length;
                log.info("拷贝滚动数据总条数=={}", response.getHits().getTotalHits());
                log.info("当前【{}】索引拷贝滚动数据条数=={}", index, copyCount);
            } catch (Exception e) {
                log.info("出错：{}", e.getMessage(), e);
            }
        }
        log.info("清洗任务结束>>>>>>>>>>>>>>>>>>>>>>");
    }


    public static void main(String[] args) {
        BoolQueryBuilder should = boolQuery().should(termsQuery("DWXS.ID", "01", "02", "03", "04", "05", "06", "07", "08"))
                .should(boolQuery().must(termQuery("DWXS.ID", "09")).must(termsQuery("DWJB.ID", "01", "02", "03")));
        should.minimumShouldMatch(1);
        System.out.println(should.toString());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        elasticSearchApi5x = builder.buildApi("es5x");
        elasticSearchApi6x = builder.buildApi("es6x");
        elasticSearchApi7x = builder.buildApi("es7x");
    }
}
