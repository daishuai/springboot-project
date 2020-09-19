package com.daishuai.mybatis.entity;

import lombok.Data;

@Data
public class UserInfoEntity {

    private String userCode;

    private String username;

    private String password;

    private Integer age;

    private String address;

    private String phone;
}
