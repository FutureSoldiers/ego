package com.ego.dubbo.service.impl;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.dubbo.service.TbContentDubboService;
import com.ego.mapper.TbContentMapper;
import com.ego.pojo.TbContent;
import com.ego.pojo.TbContentExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import javax.annotation.Resource;
import java.util.List;

public class TbContentDubboServiceImpl implements TbContentDubboService {

    @Resource
  private TbContentMapper tbContentMapper;

    @Override
    public EasyUIDataGrid selContentByPage(long categoryId, int page, int rows) {
        PageHelper.startPage(page,rows);
        TbContentExample example = new TbContentExample();
        if (categoryId!=0){
            example.createCriteria().andCategoryIdEqualTo(categoryId);
        }
        List<TbContent> list = tbContentMapper.selectByExampleWithBLOBs(example);

         PageInfo<TbContent> pageInfo = new PageInfo<>(list);

         EasyUIDataGrid dataGrid = new EasyUIDataGrid();
         dataGrid.setRows(pageInfo.getList());
         dataGrid.setTotal(pageInfo.getTotal());
         return dataGrid;
    }

    @Override
    public int insContent(TbContent content) {
        return tbContentMapper.insertSelective(content);
    }

    @Override
    public List<TbContent> selByCount(int count, boolean isSort) {
        TbContentExample example = new TbContentExample();
        //排序
        if (isSort){
            example.setOrderByClause("updated desc");
        }
        if (count!=0){
            PageHelper.startPage(1,count);
            List<TbContent> list = tbContentMapper.selectByExampleWithBLOBs(example);
            PageInfo<TbContent> pageInfo = new PageInfo<>(list);
            return pageInfo.getList();
        }else {
           return tbContentMapper.selectByExampleWithBLOBs(example);
        }
    }
}
