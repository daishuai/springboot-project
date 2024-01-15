package com.daishuai.gateway.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.daishuai.gateway.model.ServerRoute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * @author Daishuai
 * @version 1.0.0
 * @description 路由配置
 * @createTime 2024年01月09日 13:39:00
 */
@Slf4j
@Configuration
public class GatewayConfiguration {

    private static final String REWRITE_REGEX__FORM = "%s(?<remaining>/?.*)";

    private static final String REWRITE_REPLACEMENT_FORM = "%s$\\{remaining}";

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) throws MalformedURLException {
        String str = "[{\"service_id\":\"kircp-server1\",\"path\":\"/kircp/**\",\"url\":\"http://fire-dev.devdolphin.com/kircp-servers/kircp\",\"strip_prefix\":true,\"retryable\":true,\"sensitive_headers\":\"\"},{\"service_id\":\"ty-ers-service1\",\"path\":\"/ers/**\",\"url\":\"http://fire-dev.devdolphin.com/ers-service\",\"strip_prefix\":true,\"retryable\":true,\"sensitive_headers\":null},{\"service_id\":\"yj-ers-asset-server1\",\"path\":\"/asset/**\",\"url\":\"http://fire-dev.devdolphin.com/ers-asset-server/asset\",\"strip_prefix\":true,\"retryable\":true,\"sensitive_headers\":null},{\"service_id\":\"yj-ers-file-server1\",\"path\":\"/fileserver/**\",\"url\":\"https://fire-dev.devdolphin.com/ers-file-server\",\"strip_prefix\":true,\"retryable\":true,\"sensitive_headers\":null},{\"service_id\":\"cloud-rbac1\",\"path\":\"/cloudrbac/**\",\"url\":\"https://fire-dev.devdolphin.com/cloud-rbac\",\"strip_prefix\":true,\"retryable\":true,\"sensitive_headers\":null},{\"service_id\":\"yj-ers-voice-server\",\"path\":\"/voice/**\",\"url\":\"https://iacs-dev.devdolphin.com/ers-voice-server\",\"strip_prefix\":true,\"retryable\":true,\"sensitive_headers\":null},{\"service_id\":\"ty-ers-outside\",\"path\":\"/outside/**\",\"url\":\"http://fire-dev.devdolphin.com/ers-outside/outside-server\",\"strip_prefix\":true,\"retryable\":true,\"sensitive_headers\":null},{\"service_id\":\"ty-ers-file-server\",\"path\":\"/file-forward/**\",\"url\":\"http://fire-dev.devdolphin.com/ers-file-server\",\"strip_prefix\":true,\"retryable\":true,\"sensitive_headers\":null}]";
        List<ServerRoute> serverRoutes = JSON.parseObject(str, new TypeReference<List<ServerRoute>>() {});
        RouteLocatorBuilder.Builder routes = builder.routes();
        for (ServerRoute serverRoute : serverRoutes) {
            String urlStr = serverRoute.getUrl();
            URL url = new URL(urlStr);
            String regex = String.format(REWRITE_REGEX__FORM, serverRoute.getPath().replace("/**", ""));
            regex = "/gate" + regex;
            String replacement = String.format(REWRITE_REPLACEMENT_FORM, url.getPath());
            log.info("rewritePath: {}, {}", regex, replacement);
            String finalRegex = regex;
            String finalReplacement = replacement;
            routes.route(serverRoute.getServiceId(), route -> route.path(serverRoute.getPath())
                    .filters(filter -> filter.rewritePath(finalRegex, finalReplacement)).uri(urlStr));
        }
        return routes.build();
    }
}
