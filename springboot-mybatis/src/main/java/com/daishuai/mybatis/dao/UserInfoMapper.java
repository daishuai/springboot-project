package com.daishuai.mybatis.dao;

import com.daishuai.mybatis.entity.UserInfoEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInfoMapper {

    /**
     * 根据用户编号查询用户信息
     *
     * @param userCode
     * @return
     */
    UserInfoEntity findByUserCode(String userCode);

    /**
     * 查询所有用户信息
     *
     * @return
     */
    List<UserInfoEntity> findAll();

    /**
     * 分页查询用户信息
     *
     * @return
     */
    List<UserInfoEntity> pageFind();

    /**
     * 新增用户信息
     *
     * @param userInfo
     */
    int addUserInfo(UserInfoEntity userInfo);

    /**
     * 更新用户信息
     *
     * @param userInfo
     * @return
     */
    int updateUserInfo(UserInfoEntity userInfo);

    /**
     * 根据用户编号删除用户信息
     *
     * @param userCode
     * @return
     */
    int deleteUserInfo(String userCode);
}
