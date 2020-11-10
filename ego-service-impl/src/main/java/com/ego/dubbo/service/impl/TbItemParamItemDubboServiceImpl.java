package com.ego.dubbo.service.impl;

import com.ego.dubbo.service.TbItemParamItemDubboService;
import com.ego.mapper.TbItemParamItemMapper;
import com.ego.pojo.TbItemParamItem;
import com.ego.pojo.TbItemParamItemExample;

import javax.annotation.Resource;
import java.util.List;

public class TbItemParamItemDubboServiceImpl implements TbItemParamItemDubboService {

    @Resource
    private TbItemParamItemMapper tbItemParamItemMapper;

    @Override
    public TbItemParamItem selByItemId(long itemId) {
        TbItemParamItemExample example = new TbItemParamItemExample();
        example.createCriteria().andItemIdEqualTo(itemId);
        List<TbItemParamItem> list = tbItemParamItemMapper.selectByExampleWithBLOBs(example);
        if (list!=null&&list.size()>0){
            return list.get(0);
        }
        return null;
    }
}
