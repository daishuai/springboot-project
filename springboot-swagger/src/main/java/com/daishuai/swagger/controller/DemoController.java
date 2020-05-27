package com.daishuai.swagger.controller;

import com.alibaba.fastjson.JSON;
import com.daishuai.swagger.common.ResponseDto;
import com.daishuai.swagger.dto.PersonDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Daishuai
 * @date 2020/4/19 15:57
 */
@Slf4j
@Api(tags = "测试文档")
@RestController
@RequestMapping(value = "/demo")
public class DemoController {

    @ApiOperation(value = "新增")
    @PostMapping(value = "/add")
    public ResponseDto add(@RequestBody PersonDto person) throws JsonProcessingException {
        log.info("person:{}", JSON.toJSONString(person));
        String personStr = "{\n" +
                "\t\"FIRSTNAME\": \"张三\",\n" +
                "\t\"age\": 13,\n" +
                "\t\"address\": \"江苏苏州\",\n" +
                "\t\"birthday\": \"2000-12-01 13:12:11\",\n" +
                "\t\"father\": {\n" +
                "\t\t\n" +
                "\t},\n" +
                "\t\"mother\": {\n" +
                "\t\t\n" +
                "\t},\n" +
                "\t\"sons\": []\n" +
                "}";
        PersonDto personDto = JSON.parseObject(personStr, PersonDto.class);
        log.info("personDto:{}", JSON.toJSONString(personDto));
        log.info("persongDto:{}", new ObjectMapper().writeValueAsString(personDto));
        return ResponseDto.success(person);
    }
}
