package com.daishuai.demo.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Daishuai
 * @date 2020/2/11 11:02
 */
@Data
public class WaterAllocationResponse {

    /**
     * 请求成功标识
     */
    private Boolean success;

    /**
     * 错误代码
     */
    private Integer errorCode;

    private String targetId;

    private String message;

    private Integer total;

    private List contents;
}
