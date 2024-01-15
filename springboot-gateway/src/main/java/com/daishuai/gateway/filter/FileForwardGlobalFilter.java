package com.daishuai.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

/**
 * @author Daishuai
 * @version 1.0.0
 * @description 文件转发过滤器
 * @createTime 2024年01月09日 15:05:00
 */
@Slf4j
@Component
public class FileForwardGlobalFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("自定义过滤器");
        Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
        if (route == null) {
            return chain.filter(exchange);
        }
        String id = route.getId();
        if (!StringUtils.equals(id, "ty-ers-file-server")) {
            return chain.filter(exchange);
        }
        ServerHttpRequest request = exchange.getRequest();
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        String fileUrl = queryParams.getFirst("fileUrl");
        if (StringUtils.isBlank(fileUrl)) {
            log.warn("文件地址为空");
            return chain.filter(exchange);
        }
        if (queryParams.size() > 1) {
            StringBuilder builder = new StringBuilder(fileUrl);
            for (String key : queryParams.keySet()) {
                if (StringUtils.equals(key, fileUrl)) {
                    continue;
                }
                String value = queryParams.getFirst(key);
                log.info("添加请求参数, key: {}, value: {}", key, value);
                builder.append("&").append(key).append("=").append(value);
            }
            fileUrl = builder.toString();
            log.info("请求地址: {}", fileUrl);
        }
        URI mergedUrl = null;
        try {
            mergedUrl = new URI(fileUrl);
        } catch (URISyntaxException e) {
            log.error("URI不合法: {}", fileUrl);
        }
        exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, mergedUrl);
        return chain.filter(exchange);
    }

}
