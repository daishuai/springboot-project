package com.daishuai.rocketmq.entity;

import lombok.Data;

/**
 * @author admin
 * @version 1.0.0
 * @description 通用消息
 * @createTime 2023-03-25 17:33:16
 */
@Data
public class CommonMessage {

    private String id;

    private String type;

    private Object payload;
}
