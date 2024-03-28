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

    private static final long serialVersionUID = 1L;
}
