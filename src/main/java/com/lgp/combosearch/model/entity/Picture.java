package com.lgp.combosearch.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 图片实体
 * @auther: lgp
 */
@Data
public class Picture implements Serializable {

    /**
     * 标题
     */
    private String title;

    /**
     * 地址
     */
    private String url;

    private static final long serialVersionUID = 1L;
}
