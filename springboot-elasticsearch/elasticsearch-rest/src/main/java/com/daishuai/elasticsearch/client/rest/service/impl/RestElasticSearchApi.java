package com.daishuai.elasticsearch.client.rest.service.impl;

import com.daishuai.elasticsearch.client.rest.service.ElasticSearchApi;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;

import java.io.IOException;
import java.util.Map;

/**
 * @author Daishuai
 * @date 2020/10/23 17:37
 */
@Slf4j
public class RestElasticSearchApi implements ElasticSearchApi {

    private final RestHighLevelClient client;

    public RestElasticSearchApi (RestHighLevelClient client) {
        this.client = client;
    }

    @Override
    public IndexResponse index(String index, String type, String docId, Map<String, Object> data) {
        IndexRequest indexRequest = new IndexRequest().index(index).type(type).id(docId).source(data).setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        return getIndexResponse(indexRequest);
    }

    @Override
    public GetResponse get(String index, String type, String docId) {
        GetRequest getRequest = new GetRequest().index(index).type(type).id(docId);
        try {
            return client.get(getRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("查询数据出错: {}", e.getMessage(), e);
        }
        return null;
    }

    private IndexResponse getIndexResponse(IndexRequest indexRequest) {
        IndexResponse indexResponse = null;
        try {
            indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("插入数据出错: {}", e.getMessage(), e);
        }
        return indexResponse;
    }
}
