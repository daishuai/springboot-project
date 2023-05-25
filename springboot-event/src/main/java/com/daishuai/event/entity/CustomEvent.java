package com.daishuai.event.entity;

import lombok.Data;

/**
 * @author Daishuai
 * @version 1.0.0
 * @description 自定义事件
 * @createTime 2023年05月25日 15:57:00
 */
@Data
public class CustomEvent {

    private String type;

    private String payload;
}
