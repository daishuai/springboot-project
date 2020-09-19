package com.daishuai.mybatis.service.impl;

import com.daishuai.mybatis.dao.UserInfoMapper;
import com.daishuai.mybatis.dto.PageSearchDto;
import com.daishuai.mybatis.dto.UserInfoDto;
import com.daishuai.mybatis.entity.UserInfoEntity;
import com.daishuai.mybatis.exception.ServiceException;
import com.daishuai.mybatis.service.UserInfoService;
import com.daishuai.mybatis.vo.UserInfoVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public UserInfoVo findByUserCode(String userCode) {
        UserInfoEntity userInfoEntity = userInfoMapper.findByUserCode(userCode);
        if (userInfoEntity == null) {
            return null;
        }
        return this.entity2Vo(userInfoEntity);
    }

    @Override
    public List<UserInfoVo> findAll() {
        List<UserInfoEntity> all = userInfoMapper.findAll();
        if (CollectionUtils.isEmpty(all)) {
            return new LinkedList<>();
        }
        return all.stream().map(this::entity2Vo).collect(Collectors.toList());
    }

    @Override
    public Page<UserInfoVo> pageFind(PageSearchDto searchDto) {
        Integer pageNo = searchDto.getPageNo();
        Integer pageSize = searchDto.getPageSize();
        PageHelper.startPage(pageNo, pageSize);
        List<UserInfoEntity> userInfos = userInfoMapper.pageFind();
        PageInfo<UserInfoEntity> pageInfo = new PageInfo<>(userInfos);
        List<UserInfoEntity> list = pageInfo.getList();
        List<UserInfoVo> content;
        if (CollectionUtils.isEmpty(list)) {
            content = new LinkedList<>();
        } else {
            content = list.stream().map(this::entity2Vo).collect(Collectors.toList());
        }
        return new PageImpl<>(content, searchDto.pageable(), pageInfo.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfoVo addUserInfo(UserInfoDto userInfoDto) {
        String userCode = UUID.randomUUID().toString().replaceAll("-", "");
        userInfoDto.setUserCode(userCode);
        UserInfoEntity userInfoEntity = this.dto2Entity(userInfoDto);
        userInfoMapper.addUserInfo(userInfoEntity);
        return this.entity2Vo(userInfoEntity);
    }

    @Override
    public boolean updateUserInfo(UserInfoDto userInfoDto) {
        if (StringUtils.isBlank(userInfoDto.getUserCode())) {
            throw new ServiceException("用户编号不能为空");
        }
        UserInfoEntity userInfoEntity = this.dto2Entity(userInfoDto);
        int rows = userInfoMapper.updateUserInfo(userInfoEntity);
        return rows > 0;
    }

    @Override
    public boolean deleteUserInfo(String userCode) {
        int rows = userInfoMapper.deleteUserInfo(userCode);
        return rows > 0;
    }

    /**
     * Entity转Vo
     *
     * @param userInfoEntity
     * @return
     */
    private UserInfoVo entity2Vo(UserInfoEntity userInfoEntity) {
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(userInfoEntity, userInfoVo);
        return userInfoVo;
    }

    /**
     * Dto转Entity
     *
     * @param userInfoDto
     * @return
     */
    private UserInfoEntity dto2Entity(UserInfoDto userInfoDto) {
        UserInfoEntity userInfoEntity = new UserInfoEntity();
        BeanUtils.copyProperties(userInfoDto, userInfoEntity);
        return userInfoEntity;
    }
}
