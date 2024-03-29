package com.lgp.combosearch.model.dto.search;

import com.lgp.combosearch.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @auther: lgp
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SearchRequest extends PageRequest implements Serializable {

    /**
     * 搜索词
     */
    private String searchText;

    /**
     * 搜索类型
     * 非空则搜索特定类型
     * 空则为搜索全部
     */
    private String type;

    private static final long serialVersionUID = 1L;
}
