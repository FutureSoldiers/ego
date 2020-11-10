package com.ego.manage.service;

import com.ego.commons.pojo.EasyUiTree;
import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbContentCategory;

import java.util.List;

public interface TbContentCategoryService {

    /**
     * 查询所有类目并转换为easyUI tree的属性要求
     * @return
     */
    List<EasyUiTree> showCategory(long id);

    /**
     * 类目新增
     * @param category
     * @return
     */
    EgoResult create(TbContentCategory category);

    /**
     * 类目重命名
     * @param category
     * @return
     */
    EgoResult update(TbContentCategory category);

    /**
     * 删除类目
     * @param
     * @return
     */
    EgoResult delete(TbContentCategory category);

}
