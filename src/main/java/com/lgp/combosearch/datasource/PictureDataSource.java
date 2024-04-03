package com.lgp.combosearch.datasource;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lgp.combosearch.common.ErrorCode;
import com.lgp.combosearch.exception.BusinessException;
import com.lgp.combosearch.model.entity.Picture;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @auther: lgp
 */
@Component
public class PictureDataSource implements DataSource<Picture> {

    /**
     * 获取图片并返回
     * @param searchText 图片标题
     * @param pageNum 页码
     * @param pageSize 页面大小
     * @return Page
     */
    @Override
    public Page<Picture> doSearch(String searchText, long pageNum, long pageSize) {
        // 拉取图片地址
        long current = (pageNum - 1) * pageSize;
        String url = "https://cn.bing.com/images/search?q=" + searchText + "n&first=" + current;
        Document document;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "获取数据异常");
        }
        Elements elements = document.select(".iuscp.isv.smallheight");
        List<Picture> pictures = new ArrayList<>();
        for (Element element : elements) {
            // 获取图片地址
            String m = element.select(".iusc").get(0).attr("m");
            Map<String, Object> map = JSONUtil.toBean(m, Map.class);
            String murl = (String) map.get("murl");
            // 获取标题
            String title = element.select(".inflnk").get(0).attr("aria-label");

            Picture picture = new Picture();
            picture.setUrl(murl);
            picture.setTitle(title);
            pictures.add(picture);

            // 限制条数
            if (pictures.size() >= pageSize) {
                break;
            }
        }
        Page<Picture> page = new Page<>();
        page.setRecords(pictures);
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        return page;
    }
}
