package com.ego.item.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.dubbo.service.TbItemCatDubboService;
import com.ego.item.pojo.PortalMenu;
import com.ego.item.pojo.PortalMenuNode;
import com.ego.item.service.TbItemCatService;
import com.ego.pojo.TbItemCat;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class TbItemCatServiceImpl implements TbItemCatService {

   @Reference
   private TbItemCatDubboService tbItemCatDubboServiceImpl;

    @Override
    public PortalMenu showCatMenu() {
        //查询出所有一级菜单
        List<TbItemCat> list = tbItemCatDubboServiceImpl.show(0);
        if(list!=null&&list.size()>0){
            PortalMenu portalMenu = new PortalMenu();
            portalMenu.setData(selAllMenu(list));
            return portalMenu;
        }
        System.out.println("list为空");
        PortalMenu portalMenu = new PortalMenu();
        portalMenu.setData(selAllMenu(list));
        return portalMenu;
    }

    /**
     * 最终返回结果所有查询到的结果.
     */
    public List<Object> selAllMenu(List<TbItemCat> list){
        List<Object> nodeList = new ArrayList<>();
        for (TbItemCat tbItemCat : list) {
                if (tbItemCat.getIsParent()){
                PortalMenuNode menuNode = new PortalMenuNode();
                menuNode.setU("/products/"+tbItemCat.getId()+".html");
                menuNode.setN("<a href='/products/"+tbItemCat.getId()+".html'>"+tbItemCat.getName()+"</a>");
                menuNode.setI(selAllMenu(tbItemCatDubboServiceImpl.show(tbItemCat.getId())));
                nodeList.add(menuNode);
            }else {
                nodeList.add("/products/"+tbItemCat.getId()+".html|"+tbItemCat.getName());
            }
        }

      return nodeList;
    }
}
