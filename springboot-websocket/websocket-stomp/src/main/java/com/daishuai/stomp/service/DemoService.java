package com.daishuai.stomp.service;

import com.daishuai.stomp.dto.Demo1Request;
import com.daishuai.stomp.dto.DemoRequest;
import com.daishuai.stomp.dto.ResponseMessage;

/**
 * @author Daishuai
 * @date 2021/1/15 9:46
 */
public interface DemoService {

    /**
     * DEMO
     *
     * @param request 请求参数
     * @param clientIds 客户端ID
     */
    ResponseMessage demo(DemoRequest request, String ... clientIds);

    /**
     * DEMO2
     *
     * @param request
     * @param clientIds
     */
    ResponseMessage demo1(Demo1Request request, String ... clientIds);
}
