package com.lgp.combosearch.crawler;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.lgp.combosearch.common.ErrorCode;
import com.lgp.combosearch.exception.BusinessException;
import com.lgp.combosearch.model.entity.Picture;
import com.lgp.combosearch.model.entity.Post;
import com.lgp.combosearch.service.PostService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @auther: lgp
 */
@SpringBootTest
public class CrawlerTest {

    @Resource
    private PostService postService;

    @Test
    void testFetchPicture() throws IOException {
        // 拉取图片地址
        String url = "https://cn.bing.com/images/search?q=spider-gwen&first=1";
        Document document = Jsoup.connect(url).get();
        Elements elements = document.select(".iuscp.isv.smallheight");
        List<Picture> pictures = new ArrayList<>();
        for (Element element : elements) {
            // 获取图片地址
            String m = element.select(".iusc").get(0).attr("m");
            System.out.println(m);
            System.out.println();
            Map<String, Object> map = JSONUtil.toBean(m, Map.class);
            String murl = (String) map.get("murl");
            // 获取标题
            String title = element.select(".inflnk").get(0).attr("aria-label");

            Picture picture = new Picture();
            picture.setUrl(murl);
            picture.setTitle(title);
            pictures.add(picture);
        }
        System.out.println(pictures);
    }

    /**
     * 测试使用Hutool获取文章
     */
    @Test
    void testFetchPassage() {
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
        // 断言
        Assertions.assertTrue(b);
    }
}
