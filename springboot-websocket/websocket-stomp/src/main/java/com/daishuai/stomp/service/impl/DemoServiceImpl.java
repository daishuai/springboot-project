package com.daishuai.stomp.service.impl;

import com.alibaba.fastjson.JSON;
import com.daishuai.stomp.annotation.WebSocketMethod;
import com.daishuai.stomp.annotation.WebSocketService;
import com.daishuai.stomp.dto.Demo1Request;
import com.daishuai.stomp.dto.DemoRequest;
import com.daishuai.stomp.dto.ResponseMessage;
import com.daishuai.stomp.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Daishuai
 * @date 2021/1/15 9:47
 */
@Slf4j
@Service
@WebSocketService
public class DemoServiceImpl implements DemoService {

    @WebSocketMethod(businessType = "demo", taskName = "demo", intervalSeconds = 5)
    @Override
    public ResponseMessage demo(DemoRequest request, String... clientIds) {
        log.info("执行业务demo方法: {}", JSON.toJSONString(request));
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("Message From Demo >>>>>>>>>>>>>");
        return responseMessage;
    }



    @Override
    @WebSocketMethod(businessType = "demo1", taskName = "demo", intervalSeconds = 2)
    public ResponseMessage demo1(Demo1Request request, String ... clientIds) {
        log.info("执行业务demo1方法: {}", JSON.toJSONString(request));
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("Message From Demo1 >>>>>>>>>>>>>");
        return responseMessage;
    }
}
