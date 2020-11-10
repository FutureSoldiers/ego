package com.ego.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.EasyUiTree;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.utils.IDUtils;
import com.ego.dubbo.service.TbContentCategoryDubboService;
import com.ego.manage.service.TbContentCategoryService;
import com.ego.pojo.TbContentCategory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TbContentCategoryServiceImpl implements TbContentCategoryService {

    @Reference
  private TbContentCategoryDubboService tbContentCategoryDubboServiceImpl;

    @Override
    public List<EasyUiTree> showCategory(long id) {
        List<EasyUiTree> treeList = new ArrayList<>();
        List<TbContentCategory> categoryList = tbContentCategoryDubboServiceImpl.selByPid(id);
        for (TbContentCategory category : categoryList) {
            EasyUiTree tree = new EasyUiTree();

            tree.setId(category.getId());
            tree.setText(category.getName());
            tree.setState(category.getIsParent()?"closed":"open");
            treeList.add(tree);

        }

        return treeList;
    }

    @Override
    public EgoResult create(TbContentCategory category) {

        EgoResult egoResult = new EgoResult();

        //判断当前节点名称是否存在
        List<TbContentCategory> children = tbContentCategoryDubboServiceImpl.selByPid(category.getParentId());
        for (TbContentCategory child : children) {
            if (child.getName().equals(category.getName())){
                egoResult.setData("该分类名称已存在");
                return egoResult;
            }
        }

        Date date = new Date();
        category.setCreated(date);
        category.setUpdated(date);
        category.setStatus(1);
        category.setSortOrder(1);
        category.setIsParent(false);
        long id = IDUtils.genItemId();
        category.setId(id);
        int index = tbContentCategoryDubboServiceImpl.insCategory(category);
        if (index>0){
            TbContentCategory parent = new TbContentCategory();
            parent.setId(category.getParentId());
            parent.setIsParent(true);
            tbContentCategoryDubboServiceImpl.updIsParentById(parent);
        }
        egoResult.setStatus(200);
        Map<String,Long> map = new HashMap<>();
        map.put("id",id);
        egoResult.setData(map);
        return egoResult;
    }

    @Override
    public EgoResult update(TbContentCategory category) {
        EgoResult egoResult = new EgoResult();
        //查询当前节点信息
        TbContentCategory contentCategory = tbContentCategoryDubboServiceImpl.selById(category.getId());
       //查询当前节点的父节点的所有子节点信息
        List<TbContentCategory> children = tbContentCategoryDubboServiceImpl.selByPid(contentCategory.getParentId());
        for (TbContentCategory child : children) {
            if (child.getName().equals(category.getName())){
                egoResult.setData("该分类名称已存在");
                return egoResult;
            }
        }
        int index = tbContentCategoryDubboServiceImpl.updIsParentById(category);
        if (index>0){
            egoResult.setStatus(200);
        }
        return egoResult;
    }

    @Override
    public EgoResult delete(TbContentCategory category) {
        EgoResult egoResult = new EgoResult();

        category.setStatus(0);
        int index = tbContentCategoryDubboServiceImpl.updIsParentById(category);
        if (index>0){
            TbContentCategory contentCategory = tbContentCategoryDubboServiceImpl.selById(category.getId());
            List<TbContentCategory> list = tbContentCategoryDubboServiceImpl.selByPid(contentCategory.getParentId());
            if (list==null||list.size()==0){
                TbContentCategory parent = new TbContentCategory();
                parent.setId(contentCategory.getParentId());
                parent.setIsParent(false);
                int result = tbContentCategoryDubboServiceImpl.updIsParentById(parent);
                if (result>0){
                    egoResult.setStatus(200);
                }
            }else {
                egoResult.setStatus(200);
            }
        }
        return egoResult;
    }
}
