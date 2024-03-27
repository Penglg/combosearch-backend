package com.lgp.combosearch.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lgp.combosearch.common.BaseResponse;
import com.lgp.combosearch.common.ErrorCode;
import com.lgp.combosearch.common.ResultUtils;
import com.lgp.combosearch.exception.ThrowUtils;
import com.lgp.combosearch.model.dto.picture.PictureQueryRequest;
import com.lgp.combosearch.model.entity.Picture;
import com.lgp.combosearch.service.PictureService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 图片服务
 * @auther: lgp
 */
@RestController
@RequestMapping("/picture")
public class PictureController {

    @Resource
    private PictureService pictureService;

    /**
     * 获取图片
     * @param pictureQueryRequest 图片请求数据
     * @param request request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<Picture>> listPictureByPage(@RequestBody PictureQueryRequest pictureQueryRequest,
                                                         HttpServletRequest request) {
        long pageNum = pictureQueryRequest.getCurrent();
        long pageSize = pictureQueryRequest.getPageSize();
        // 限制爬虫，不允许一次获取大于20条数据
        ThrowUtils.throwIf(pageSize > 20, ErrorCode.PARAMS_ERROR);
        String searchText = pictureQueryRequest.getSearchText();
        Page<Picture> page = pictureService.searchPictures(searchText, pageNum, pageSize);
        return ResultUtils.success(page);
    }
}
