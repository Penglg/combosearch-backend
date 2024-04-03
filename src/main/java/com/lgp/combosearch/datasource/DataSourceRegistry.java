package com.lgp.combosearch.datasource;

import com.lgp.combosearch.model.enums.SearchTypeEnum;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 注册模式
 *
 * @auther: lgp
 */
@Component
public class DataSourceRegistry {

    @Resource
    private PictureDataSource pictureDataSource;

    @Resource
    private PostDataSource postDataSource;

    @Resource
    private UserDataSource userDataSource;

    private Map<String, DataSource<?>> typeDataSourceMap;

    @PostConstruct
    void init() {
        typeDataSourceMap = new HashMap<String, DataSource<?>>(){{
            put(SearchTypeEnum.POST.getValue(), postDataSource);
            put(SearchTypeEnum.PICTURE.getValue(), pictureDataSource);
            put(SearchTypeEnum.USER.getValue(), userDataSource);
        }};
    }

    public DataSource<?> getDataSourceByType(String type) {
        if (typeDataSourceMap == null) {
            return null;
        }
        return typeDataSourceMap.get(type);
    }
}
