package com.daishuai.mybatis.dto;

import lombok.Data;

@Data
public class UserInfoDto {

    private String userCode;

    private String username;

    private String password;

    private Integer age;

    private String address;

    private String phone;
}
