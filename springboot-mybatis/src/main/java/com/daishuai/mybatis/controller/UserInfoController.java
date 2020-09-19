package com.daishuai.mybatis.controller;

import com.daishuai.mybatis.dto.PageSearchDto;
import com.daishuai.mybatis.dto.UserInfoDto;
import com.daishuai.mybatis.service.UserInfoService;
import com.daishuai.mybatis.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/userInfo")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 根据用户编号查询用户
     *
     * @param userCode
     * @return
     */
    @GetMapping(value = "/{userCode}")
    public UserInfoVo findUserByCode(@PathVariable(name = "userCode") String userCode) {
        return userInfoService.findByUserCode(userCode);
    }

    /**
     * 查询所有用户
     *
     * @return
     */
    @GetMapping(value = "")
    public List<UserInfoVo> findAll() {
        return userInfoService.findAll();
    }

    /**
     * 分页查询用户信息
     *
     * @param searchDto
     * @return
     */
    @GetMapping(value = "/page")
    public Page<UserInfoVo> pageFind(PageSearchDto searchDto) {
        return userInfoService.pageFind(searchDto);
    }

    /**
     * 新增用户
     *
     * @param userInfoDto
     */
    @PostMapping(value = "")
    public UserInfoVo addUserInfo(@RequestBody UserInfoDto userInfoDto) {
        return userInfoService.addUserInfo(userInfoDto);
    }

    /**
     * 更新用户信息
     *
     * @param userInfoDto
     * @return
     */
    @PutMapping(value = "")
    public boolean updateUserInfo(@RequestBody UserInfoDto userInfoDto) {
        return userInfoService.updateUserInfo(userInfoDto);
    }

    /**
     * 根据用户编号删除用户信息
     *
     * @param userCode
     * @return
     */
    @DeleteMapping(value = "/{userCode}")
    public boolean deleteUserInfo(@PathVariable(name = "userCode") String userCode) {
        return userInfoService.deleteUserInfo(userCode);
    }
}
