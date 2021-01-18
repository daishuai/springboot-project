package com.daishuai.stomp.dto;

import lombok.Data;

/**
 * @author Daishuai
 * @date 2021/1/15 16:03
 */
@Data
public class InstantResponse {

    private Long timestamp;

    private Object payload;
}
