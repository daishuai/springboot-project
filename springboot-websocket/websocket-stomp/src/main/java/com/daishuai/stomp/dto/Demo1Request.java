package com.daishuai.stomp.dto;

import lombok.Data;

/**
 * @author Daishuai
 * @date 2021/1/15 15:49
 */
@Data
public class Demo1Request extends InstantRequest {

    private String incidentCode;

    @Override
    public String getParamKey() {
        return this.getIncidentCode();
    }
}
