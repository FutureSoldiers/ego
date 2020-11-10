package com.ego.portal.controller;

import com.ego.portal.service.TbContentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
public class TbContentController {

    @Resource
    private TbContentService tbContentServiceImpl;

    @RequestMapping("showBigPic")
    public String showBigPic(Model model){
//        System.out.println(tbContentServiceImpl.showBigPic());
         model.addAttribute("ad1",tbContentServiceImpl.showBigPic());
        return "index";
    }
}
