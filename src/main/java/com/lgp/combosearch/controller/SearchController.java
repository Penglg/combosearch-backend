package com.lgp.combosearch.controller;

import com.lgp.combosearch.common.BaseResponse;
import com.lgp.combosearch.common.ResultUtils;
import com.lgp.combosearch.manager.SearchFacade;
import com.lgp.combosearch.model.dto.search.SearchRequest;
import com.lgp.combosearch.model.vo.SearchVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 搜索服务
 * @auther: lgp
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    @Resource
    private SearchFacade searchFacade;

    @PostMapping("/all")
    public BaseResponse<SearchVO> searchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        return ResultUtils.success(searchFacade.searchAll(searchRequest, request));
    }
}
