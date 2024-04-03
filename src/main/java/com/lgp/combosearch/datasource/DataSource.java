package com.lgp.combosearch.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 新数据源接口
 * 要求接入系统的数据源都要实现
 * @auther: lgp
 */
public interface DataSource<T> {

    /**
     * 数据搜索
     *
     * @param searchText 搜索内容
     * @param pageNum 当前页
     * @param pageSize 页面大小
     * @return
     */
    Page<T> doSearch(String searchText, long pageNum, long pageSize);
}
