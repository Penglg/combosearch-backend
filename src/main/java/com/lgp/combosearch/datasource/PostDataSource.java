package com.lgp.combosearch.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lgp.combosearch.model.dto.post.PostQueryRequest;
import com.lgp.combosearch.model.entity.Post;
import com.lgp.combosearch.model.vo.PostVO;
import com.lgp.combosearch.service.PostService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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


        List<Post> posts = postService.searchFromEs(postQueryRequest);
        Page<PostVO> postVOPage = new Page<>();
        postVOPage.setTotal(posts.size());
        List<PostVO> postVOs = posts.stream().map(post -> {
            PostVO postVO = new PostVO();
            BeanUtils.copyProperties(post, postVO);
            return postVO;
        }).collect(Collectors.toList());
        postVOPage.setRecords(postVOs);

        return postVOPage;
    }
}




