package com.ego.item.controller;

import com.ego.commons.pojo.TbItemChild;
import com.ego.item.service.TbItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
public class TbItemController {

    @Resource
    private TbItemService tbItemServiceImpl;

    /**
     * 显示商品详细信息
     * @param id
     * @return
     */
    @RequestMapping("item/{id}.html")
    public String showItemDetails(@PathVariable long id,Model model){
        TbItemChild show = tbItemServiceImpl.show(id);
//        String[] images = show.getImages();
//        for (String image : images) {
//            System.out.println(image);
//        }
        model.addAttribute("item",show);
        return "item";
    }
}
