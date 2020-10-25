package com.daishuai.elasticsearch.client.rest.service;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;

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
     * 根据ID检索数据
     *
     * @param index
     * @param type
     * @param docId
     * @return
     */
    GetResponse get(String index, String type, String docId);
}
