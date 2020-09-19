package com.daishuai.mybatis.service;

import com.daishuai.mybatis.dto.PageSearchDto;
import com.daishuai.mybatis.dto.UserInfoDto;
import com.daishuai.mybatis.vo.UserInfoVo;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserInfoService {

    /**
     * 根据用户编号查询用户
     *
     * @param userCode
     * @return
     */
    UserInfoVo findByUserCode(String userCode);

    /**
     * 查询所有用户
     *
     * @return
     */
    List<UserInfoVo> findAll();

    /**
     * 分页查询用户信息
     *
     * @param searchDto
     * @return
     */
    Page<UserInfoVo> pageFind(PageSearchDto searchDto);

    /**
     * 新增用户信息
     *
     * @param userInfoDto
     */
    UserInfoVo addUserInfo(UserInfoDto userInfoDto);

    /**
     * 更新用户信息
     *
     * @param userInfoDto
     * @return
     */
    boolean updateUserInfo(UserInfoDto userInfoDto);

    /**
     * 删除用户信息
     *
     * @param userCode
     * @return
     */
    boolean deleteUserInfo(String userCode);


}
