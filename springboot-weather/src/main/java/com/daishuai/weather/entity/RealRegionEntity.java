package com.daishuai.weather.entity;

import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Daishuai
 * @date 2020/11/4 12:02
 */
@Data
@Table(name = "real_region")
@Entity
public class RealRegionEntity {

    @Id
    private Long id;

    /**
     * 统一权限code关联
     */
    private String code;

    /****
     * 统一权限行政名称
     */
    private String name;

    /**
     * 行政编码
     */
    @Basic
    private String xzbm;

    /**
     * 行政名称
     */
    @Basic
    private String xzmc;

    /**
     * 行政级别
     */
    @Basic
    private Short xzjb;

    /**
     * 行政区号
     */
    @Basic
    private String xzqh;

    /**
     * 行政内部编码
     */
    @Basic
    private String xznbbm;

    /**
     * 行政简称
     */
    @Basic
    private String xzjc;

    /**
     * 行政顺序
     */
    @Basic
    private String xssx;

    /**
     * 是否可用
     */
    @Basic
    private String sfky;

    /**
     * 拼音
     */
    @Basic
    private String qypy;

    /**
     * 预留字段1
     */
    @Basic
    private String ylzd1;

    /**
     * 预留字段2
     */
    @Basic
    private String ylzd2;

    /**
     * 预留字段3
     */
    @Basic
    private String ylzd3;

    /**
     * 政府机关
     */
    @Basic
    private String zfjg;

    /**
     * 范围xmin
     */
    @Basic
    private String xmin;

    /**
     * 范围ymin
     */
    @Basic
    private String ymin;

    /**
     * 范围xmax
     */
    @Basic
    private String xmax;

    /**
     * 范围ymax
     */
    @Basic
    private String ymax;

    /**
     * 行政父编码
     */
    @Basic
    private String xzfbm;

    /**
     * 名称简称
     */
    @Basic
    private String mcjc;

    @Basic
    private String xcenter;

    @Basic
    private String ycenter;
}
