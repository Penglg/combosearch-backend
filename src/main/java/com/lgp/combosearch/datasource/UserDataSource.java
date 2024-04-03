package com.lgp.combosearch.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lgp.combosearch.model.dto.user.UserQueryRequest;
import com.lgp.combosearch.model.vo.UserVO;
import com.lgp.combosearch.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 用户服务实现
 *
 */
@Component
public class UserDataSource implements DataSource<UserVO> {

    @Resource
    private UserService userService;

    @Override
    public Page<UserVO> doSearch(String searchText, long pageNum, long pageSize) {
        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setUserName(searchText);
        userQueryRequest.setCurrent(pageNum);
        userQueryRequest.setPageSize(pageSize);

        return userService.listUserVOByPage(userQueryRequest);
    }
}