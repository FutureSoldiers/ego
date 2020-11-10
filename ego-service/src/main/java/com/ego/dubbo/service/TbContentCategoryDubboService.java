package com.ego.dubbo.service;

import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbContentCategory;

import java.util.List;

public interface TbContentCategoryDubboService {

    /**
     * 根据父id查询所有子类目
     * @return
     */
    List<TbContentCategory> selByPid(long id);

    /**
     * 新增类目
     * @param category
     * @return
     */
    int insCategory(TbContentCategory category);

    /**
     * 修改isparent通过id
     * @param
     * @return
     */
    int updIsParentById(TbContentCategory category);

    /**
     * 通过id查询内容类目详细信息
     * @param id
     * @return
     */
    TbContentCategory selById(long id);


}


