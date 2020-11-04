package com.daishuai.elasticsearch.client.rest.service;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.Map;

/**
 * @author Daishuai
 * @date 2020/10/23 17:31
 */
public interface ElasticSearchApi {

    /**
     * 新增数据
     *
     * @param index index
     * @param type type
     * @param docId docId
     * @param data data
     * @return response
     */
    IndexResponse index(String index, String type, String docId, Map<String, Object> data);

    /**
     * 更新数据
     *
     * @param index
     * @param type
     * @param docId
     * @param data
     * @return
     */
    UpdateResponse update(String index, String type, String docId, Map<String, Object> data);

    /**
     * 存在则更新，不存在则新增
     *
     * @param index
     * @param type
     * @param docId
     * @param data
     * @return
     */
    UpdateResponse upsert(String index, String type, String docId, Map<String, Object> data);


    /**
     * 根据ID检索数据
     *
     * @param index
     * @param type
     * @param docId
     * @return
     */
    GetResponse get(String index, String type, String docId);

    /**
     * 根据条件检索数据
     *
     * @param index
     * @param type
     * @param source
     * @return
     */
    SearchResponse search(String index, String type, SearchSourceBuilder source);

    /**
     * 根据ID检索数据
     *
     * @param index
     * @param type
     * @param docId
     * @param includes 指定字段
     * @return
     */
    GetResponse get(String index, String type, String docId, String ... includes);
}
