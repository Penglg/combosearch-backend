package com.lgp.combosearch.job.once;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.lgp.combosearch.common.ErrorCode;
import com.lgp.combosearch.exception.BusinessException;
import com.lgp.combosearch.model.entity.Post;
import com.lgp.combosearch.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 项目运行时执行的一次任务，抓取数据
 * 取消@Component注释后生效
 *
 * @auther: lgp
 */
//@Component
@Slf4j
public class FetchInitPostList implements CommandLineRunner {

    @Resource
    private PostService postService;

    @Override
    public void run(String... args) throws Exception {
        // 发送Restful请求
        String json = "{\"current\":1,\"pageSize\":8,\"sortField\":\"createTime\",\"sortOrder\":\"descend\",\"category\":\"文章\",\"reviewStatus\":1}";
        String url = "https://www.code-nav.cn/api/post/search/page/vo";
        String result = HttpRequest.post(url)
                .body(json)
                .execute().body();

        Map<String, Object> map = JSONUtil.toBean(result, Map.class);
        if (!map.get("code").equals(0)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }

        JSONObject data = (JSONObject) map.get("data");
        JSONArray records = (JSONArray) data.get("records");
        List<Post> postList = new ArrayList<>(records.size());
        for (Object record : records) {
            Post post = new Post();
            JSONObject recordJSON = (JSONObject)record;
            // TODO: 2024/3/26 参数校验，是否为空
            post.setTitle((String) recordJSON.get("title"));
            post.setContent((String) recordJSON.get("content"));
            List<String> tags = ((JSONArray) recordJSON.get("tags")).toList(String.class);
            post.setTags(JSONUtil.toJsonStr(tags));
            post.setUserId(1772156688787333122L);
            postList.add(post);
        }

        // 插入数据
        boolean b = postService.saveBatch(postList);
        if (b) {
            log.info("-- FetchInitPostList初始化Post数据成功");
        } else {
            log.error("-- FetchInitPostList初始化Post数据失败");
        }
    }
}
