package com.daishuai.stomp.dto;

import lombok.Data;

/**
 * @author Daishuai
 * @date 2021/1/15 9:48
 */
@Data
public class DemoRequest extends InstantRequest {

    private String departmentCode;

    @Override
    public String getParamKey() {
        return this.getDepartmentCode();
    }
}
