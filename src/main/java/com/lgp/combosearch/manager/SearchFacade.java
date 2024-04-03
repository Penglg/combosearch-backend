package com.lgp.combosearch.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lgp.combosearch.datasource.*;
import com.lgp.combosearch.model.dto.search.SearchRequest;
import com.lgp.combosearch.model.entity.Picture;
import com.lgp.combosearch.model.enums.SearchTypeEnum;
import com.lgp.combosearch.model.vo.PostVO;
import com.lgp.combosearch.model.vo.SearchVO;
import com.lgp.combosearch.model.vo.UserVO;
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
    private PictureDataSource pictureDataSource;

    @Resource
    private PostDataSource postDataSource;

    @Resource
    private UserDataSource userDataSource;

    @Resource
    private DataSourceRegistry dataSourceRegistry;

    /**
     * 执行数据的搜索
     * @param searchRequest 搜索
     * @param request request
     * @return 搜索封装
     */
    public SearchVO searchAll(SearchRequest searchRequest, HttpServletRequest request) {
        String searchText = searchRequest.getSearchText();
        long current = searchRequest.getCurrent();
        long size = searchRequest.getPageSize();
        String type = searchRequest.getType();
        SearchTypeEnum typeEnum = SearchTypeEnum.getEnumByValue(type);

        Page<Picture> picturePage;
        Page<UserVO> userVOPage;
        Page<PostVO> postVOPage;
        SearchVO searchVO = new SearchVO();

        if (typeEnum == null) {
            picturePage = pictureDataSource.doSearch(searchText, current, size);
            userVOPage = userDataSource.doSearch(searchText, current, size);
            postVOPage = postDataSource.doSearch(searchText, current, size);

            searchVO.setPictureList(picturePage.getRecords());
            searchVO.setPostList(postVOPage.getRecords());
            searchVO.setUserList(userVOPage.getRecords());
        } else {
            DataSource<?> dataSource = dataSourceRegistry.getDataSourceByType(type);
            Page<?> page = dataSource.doSearch(searchText, current, size);
            searchVO.setDataList(page.getRecords());
        }

        return searchVO;
    }
}
