package com.lgp.combosearch.model.vo;

import com.lgp.combosearch.model.entity.Picture;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @auther: lgp
 */
@Data
public class SearchVO implements Serializable {

    private List<UserVO> userList;

    private List<PostVO> postList;

    private List<Picture> pictureList;

    private List<?> dataList;

    private static final long serialVersionUID = 1L;
}
