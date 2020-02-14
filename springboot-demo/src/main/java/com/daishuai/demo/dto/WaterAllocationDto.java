package com.daishuai.demo.dto;

import lombok.Data;

/**
 * @author Daishuai
 * @date 2020/2/10 17:01
 */
@Data
public class WaterAllocationDto {

    private String accountUuid;

    private String accountName;

    private String departmentUuid;

    private String departmentName;

    private String waterSources;
}
