package com.daishuai.elasticsearch.client.rest.handler;

import lombok.Data;

import java.util.Map;

/**
 * @author Daishuai
 * @date 2021/1/12 12:20
 */
public interface EsFailureHandler {

    /**
     * 失败处理器
     * @param index
     * @param type
     * @param id
     * @param opType
     * @param doc
     * @param failure
     */
    void handleFailure(String index, String type, String id, String opType, Map<String, Object> doc, Throwable failure);
}
