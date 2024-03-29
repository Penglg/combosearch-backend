package com.lgp.combosearch.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lgp.combosearch.model.dto.post.PostQueryRequest;
import com.lgp.combosearch.model.dto.search.SearchRequest;
import com.lgp.combosearch.model.dto.user.UserQueryRequest;
import com.lgp.combosearch.model.entity.Picture;
import com.lgp.combosearch.model.enums.SearchTypeEnum;
import com.lgp.combosearch.model.vo.PostVO;
import com.lgp.combosearch.model.vo.SearchVO;
import com.lgp.combosearch.model.vo.UserVO;
import com.lgp.combosearch.service.PictureService;
import com.lgp.combosearch.service.PostService;
import com.lgp.combosearch.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 搜索门面
 * @auther: lgp
 */
@Component
public class SearchFacade {

    @Resource
    private UserService userService;

    @Resource
    private PostService postService;

    @Resource
    private PictureService pictureService;

    public SearchVO searchAll(SearchRequest searchRequest, HttpServletRequest request) {
        String searchText = searchRequest.getSearchText();
        long current = searchRequest.getCurrent();
        long size = searchRequest.getPageSize();
        String type = searchRequest.getType();
        SearchTypeEnum typeEnum = SearchTypeEnum.getEnumByValue(type);

        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setUserName(searchText);
        userQueryRequest.setCurrent(current);
        userQueryRequest.setPageSize(size);

        PostQueryRequest postQueryRequest = new PostQueryRequest();
        postQueryRequest.setTitle(searchText);
        postQueryRequest.setContent(searchText);
        postQueryRequest.setCurrent(current);
        postQueryRequest.setPageSize(size);

        Page<Picture> picturePage;
        Page<UserVO> userVOPage;
        Page<PostVO> postVOPage;
        SearchVO searchVO = new SearchVO();

        if (typeEnum == null) {
            picturePage = pictureService.searchPictures(searchText, current, size);
            userVOPage = userService.listUserVOByPage(userQueryRequest);
            postVOPage = postService.listPostVOByPage(postQueryRequest, request);

            searchVO.setPictureList(picturePage.getRecords());
            searchVO.setPostList(postVOPage.getRecords());
            searchVO.setUserList(userVOPage.getRecords());
        } else {
            switch (typeEnum) {
                case PICTURE:
                    picturePage = pictureService.searchPictures(searchText, current, size);
                    searchVO.setPictureList(picturePage.getRecords());
                    break;
                case USER:
                    userVOPage = userService.listUserVOByPage(userQueryRequest);
                    searchVO.setUserList(userVOPage.getRecords());
                    break;
                case POST:
                    postVOPage = postService.listPostVOByPage(postQueryRequest, request);
                    searchVO.setPostList(postVOPage.getRecords());
                    break;
            }
        }

        return searchVO;
    }
}
