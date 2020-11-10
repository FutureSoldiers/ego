package com.ego.dubbo.service.impl;

import com.ego.dubbo.service.TbItemDescDubboService;
import com.ego.mapper.TbItemDescMapper;
import com.ego.pojo.TbItemDesc;

import javax.annotation.Resource;

public class TbItemDescDubboServiceImpl implements TbItemDescDubboService {

    @Resource
   private TbItemDescMapper tbItemDescMapper;

    @Override
    public int insDesc(TbItemDesc itemDesc) {
        return tbItemDescMapper.insert(itemDesc);
    }

    @Override
    public TbItemDesc selByItemId(long itemId) {
        return  tbItemDescMapper.selectByPrimaryKey(itemId);
    }
}
