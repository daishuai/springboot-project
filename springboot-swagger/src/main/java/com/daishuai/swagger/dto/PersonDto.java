package com.daishuai.swagger.dto;

import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Daishuai
 * @date 2020/4/19 16:07
 */
@Data
@ApiModel
@JSONType(naming = PropertyNamingStrategy.PascalCase)
@JsonNaming(value = com.fasterxml.jackson.databind.PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class PersonDto {

    @ApiModelProperty(value = "姓名")
    private String firstName;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "出生日期")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birthday;

    @ApiModelProperty(value = "父亲")
    private PersonDto father;

    @ApiModelProperty(value = "母亲")
    private PersonDto mother;

    @ApiModelProperty(value = "子女")
    private List<PersonDto> sons;

    public static void main(String[] args) {

    }
}
