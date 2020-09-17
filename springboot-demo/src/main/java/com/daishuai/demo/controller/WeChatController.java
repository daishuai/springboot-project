package com.daishuai.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * @author Daishuai
 * @date 2020/9/17 13:31
 */
@Slf4j
@RestController
public class WeChatController {

    private static final String WECHAT_TOKEN = "SpringSocial-WeChat";

    @RequestMapping(value = "/wx.do")
    public String get(HttpServletRequest request) {

        log.error("WechatController   ----   WechatController");
        log.info("请求进来了...");

        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            String value = request.getParameter(name);
            log.info("name: {}, value: {}", name, value);
        }
        /// 微信加密签名
        String signature = request.getParameter("signature");
        /// 时间戳
        String timestamp = request.getParameter("timestamp");
        /// 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");
        log.info("signature: {}, timestamp: {}, nonce: {}, echostr: {}", signature, timestamp, nonce, echostr);
        return echostr;
    }

}
