package com.daishuai.mq.rocket.dto;

import lombok.Data;

/**
 * @author Daishuai
 * @date 2020/10/12 18:59
 */
@Data
public class MessageDto<T> {

    private String type;

    private T body;

    private String source;
}
