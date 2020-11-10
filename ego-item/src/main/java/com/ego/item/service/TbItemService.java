package com.ego.item.service;

import com.ego.commons.pojo.TbItemChild;
import com.ego.pojo.TbItem;

public interface TbItemService {

    /**
     * 显示商品详情
     * @param id
     * @return
     */
    TbItemChild show(long id);
}
