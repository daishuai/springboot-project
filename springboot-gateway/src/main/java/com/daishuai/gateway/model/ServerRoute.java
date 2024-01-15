package com.daishuai.gateway.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Daishuai
 * @version 1.0.0
 * @description 服务路由
 * @createTime 2024年01月09日 13:42:00
 */
@Data
public class ServerRoute {
    @JSONField(name = "service_id")
    private String serviceId;

    private String path;

    private String url;

    @JSONField(name = "strip_prefix")
    private boolean stripPrefix;

    private boolean retryable;

    @JSONField(name = "sensitive_headers")
    private String sensitiveHeaders;
}
