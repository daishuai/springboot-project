package com.daishuai.weather.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @author Daishuai
 * @date 2020/7/28 8:48
 */
@Data
@Table(name = "region")
@Entity
public class RegionEntity {

    @Id
    @Column(name = "code", length = 20)
    private String code;

    @Column(name = "name", length = 30)
    private String name;

    @Column(name = "level", length = 2)
    private Integer level;

    @Column(name = "parent_code", length = 20)
    private String parentCode;

    private String realCode;

    private String realName;

    private String realParentCode;

    @Transient
    private List<RegionEntity> children;

}
