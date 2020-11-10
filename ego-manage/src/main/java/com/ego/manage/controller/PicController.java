package com.ego.manage.controller;

import com.ego.manage.service.PicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

@Controller
public class PicController {

    @Autowired
    private PicService picService;

    /**
     * 文件上传
     * @param uploadFile
     * @return
     */
    @RequestMapping(value = "pic/upload",produces = {"application/json"})
    @ResponseBody
    public Map<String,Object> upload(MultipartFile uploadFile){
       return picService.upload(uploadFile);
    }
}
