package com.daishuai.distribute.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author Daishuai
 * @date 2020/9/22 13:13
 */
@Data
public class DistributeLockEntity {

    private Long id;

    private String uniqueMutex;

    private String holderId;

    private Date createTime;
}
