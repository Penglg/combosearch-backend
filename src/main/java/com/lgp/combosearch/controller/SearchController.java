package com.lgp.combosearch.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lgp.combosearch.common.BaseResponse;
import com.lgp.combosearch.common.ResultUtils;
import com.lgp.combosearch.model.dto.post.PostQueryRequest;
import com.lgp.combosearch.model.dto.search.SearchRequest;
import com.lgp.combosearch.model.dto.user.UserQueryRequest;
import com.lgp.combosearch.model.entity.Picture;
import com.lgp.combosearch.model.vo.PostVO;
import com.lgp.combosearch.model.vo.SearchVO;
import com.lgp.combosearch.model.vo.UserVO;
import com.lgp.combosearch.service.PictureService;
import com.lgp.combosearch.service.PostService;
import com.lgp.combosearch.service.UserService;
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
    private UserService userService;

    @Resource
    private PostService postService;

    @Resource
    private PictureService pictureService;

    @PostMapping("/all")
    public BaseResponse<SearchVO> searchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        String searchText = searchRequest.getSearchText();
        long current = searchRequest.getCurrent();
        long size = searchRequest.getPageSize();
        Page<Picture> picturePage = pictureService.searchPictures(searchText, current, size);

        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setUserName(searchText);
        userQueryRequest.setCurrent(current);
        userQueryRequest.setPageSize(size);
        Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);

        PostQueryRequest postQueryRequest = new PostQueryRequest();
        postQueryRequest.setTitle(searchText);
        postQueryRequest.setContent(searchText);
        postQueryRequest.setCurrent(current);
        postQueryRequest.setPageSize(size);
        Page<PostVO> postVOPage = postService.listPostVOByPage(postQueryRequest, request);

        SearchVO searchVO = new SearchVO();
        searchVO.setPictureList(picturePage.getRecords());
        searchVO.setPostList(postVOPage.getRecords());
        searchVO.setUserList(userVOPage.getRecords());

        return ResultUtils.success(searchVO);
    }
}
