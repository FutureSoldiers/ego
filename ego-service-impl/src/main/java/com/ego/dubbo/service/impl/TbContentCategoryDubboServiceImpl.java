package com.ego.dubbo.service.impl;

import com.ego.dubbo.service.TbContentCategoryDubboService;
import com.ego.mapper.TbContentCategoryMapper;
import com.ego.pojo.TbContentCategory;
import com.ego.pojo.TbContentCategoryExample;

import javax.annotation.Resource;
import java.util.List;

public class TbContentCategoryDubboServiceImpl implements TbContentCategoryDubboService {

    @Resource
    private TbContentCategoryMapper tbContentCategoryMapper;

    @Override
    public List<TbContentCategory> selByPid(long id) {
        TbContentCategoryExample example = new TbContentCategoryExample();
        example.createCriteria().andParentIdEqualTo(id).andStatusEqualTo(1);
        return  tbContentCategoryMapper.selectByExample(example);
    }

    @Override
    public int insCategory(TbContentCategory category) {
        return tbContentCategoryMapper.insertSelective(category);
    }

    @Override
    public int updIsParentById(TbContentCategory category) {
        return tbContentCategoryMapper.updateByPrimaryKeySelective(category);
    }

    @Override
    public TbContentCategory selById(long id) {
        return tbContentCategoryMapper.selectByPrimaryKey(id);
    }


}
