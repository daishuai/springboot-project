package com.daishuai.elasticsearch.client.rest.handler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author Daishuai
 * @date 2021/1/12 12:20
 */
@Slf4j
public class DefaultEsFailureHandler implements EsFailureHandler{
    @Override
    public void handleFailure(String index, String type, String id, String opType, Map<String, Object> doc, Throwable failure) {
        log.error("index:{}, type:{}, id:{}, opType:{}, doc:{}, failureMessage:{}", index, type, id, opType, JSON.toJSONString(doc), failure.getMessage(), failure);
    }
}
