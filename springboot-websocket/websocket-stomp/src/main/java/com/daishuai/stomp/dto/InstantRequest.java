package com.daishuai.stomp.dto;

import lombok.Data;

/**
 * @author Daishuai
 * @date 2021/1/14 19:19
 */
@Data
public abstract class InstantRequest {

    private String clientId;

    private String businessType;

    /**
     * 获取参数唯一标识
     *
     * @return 参数唯一标识
     */
    public abstract String getParamKey();
}
