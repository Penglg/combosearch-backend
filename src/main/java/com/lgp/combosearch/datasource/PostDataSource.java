package com.lgp.combosearch.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lgp.combosearch.model.dto.post.PostQueryRequest;
import com.lgp.combosearch.model.vo.PostVO;
import com.lgp.combosearch.service.PostService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子服务实现
 *
 */
@Component
public class PostDataSource implements DataSource<PostVO> {

    @Resource
    private PostService postService;

    @Override
    public Page<PostVO> doSearch(String searchText, long pageNum, long pageSize) {
        // 封装参数
        PostQueryRequest postQueryRequest = new PostQueryRequest();
        postQueryRequest.setTitle(searchText);
        postQueryRequest.setContent(searchText);
        postQueryRequest.setCurrent(pageNum);
        postQueryRequest.setPageSize(pageSize);

        // TODO: 2024/3/29 request参数的优化
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        if (servletRequestAttributes != null) {
            request = servletRequestAttributes.getRequest();
        }
        return postService.listPostVOByPage(postQueryRequest, request);
    }
}




