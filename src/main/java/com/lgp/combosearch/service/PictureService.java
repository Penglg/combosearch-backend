package com.lgp.combosearch.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lgp.combosearch.model.entity.Picture;

/**
 * 图片服务接口
 * @auther: lgp
 */
public interface PictureService {

    Page<Picture> searchPictures(String searchText, long pageNum, long pageSize);
}
