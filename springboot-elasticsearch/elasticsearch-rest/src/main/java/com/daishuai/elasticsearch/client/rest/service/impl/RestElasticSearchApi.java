package com.daishuai.elasticsearch.client.rest.service.impl;

import com.daishuai.elasticsearch.client.rest.service.ElasticSearchApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
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
    public UpdateResponse update(String index, String type, String docId, Map<String, Object> data) {
        UpdateRequest updateRequest = new UpdateRequest().index(index).type(type).id(docId).doc(data).docAsUpsert(false);
        return getUpdateResponse(updateRequest);
    }

    @Override
    public UpdateResponse upsert(String index, String type, String docId, Map<String, Object> data) {
        UpdateRequest updateRequest = new UpdateRequest().index(index).type(type).id(docId).doc(data).docAsUpsert(true);
        return getUpdateResponse(updateRequest);
    }

    @Override
    public GetResponse get(String index, String type, String docId) {
        return this.get(index, type, docId, Strings.EMPTY_ARRAY);
    }

    @Override
    public GetResponse get(String index, String type, String docId, String... includes) {
        GetRequest getRequest = new GetRequest().index(index).type(type).id(docId);
        if (ArrayUtils.isNotEmpty(includes)) {
            getRequest.fetchSourceContext(new FetchSourceContext(true, includes, Strings.EMPTY_ARRAY));
        }
        return this.get(getRequest);
    }

    private GetResponse get(GetRequest getRequest) {
        GetResponse getResponse = null;
        try {
            getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("查询数据出错: {}", e.getMessage(), e);
        }
        return getResponse;
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

    private UpdateResponse getUpdateResponse(UpdateRequest updateRequest) {
        UpdateResponse updateResponse = null;
        try {
            updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("更新数据出错: {}", e.getMessage(), e);
        }
        return updateResponse;
    }
}
