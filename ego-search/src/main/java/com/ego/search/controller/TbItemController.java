package com.ego.search.controller;

import com.ego.pojo.TbItem;
import com.ego.commons.pojo.TbItemChild;
import com.ego.search.service.TbItemService;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TbItemController {

    @Resource
    private TbItemService tbItemServiceImpl;

    /**
     * solr数据初始化
     * @return
     */
    @RequestMapping(value = "solr/init",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String init(){
        long start = System.currentTimeMillis();
        try {
            tbItemServiceImpl.init();
         long end =System.currentTimeMillis();
         return "初始化总时间:"+(end-start)/1000+"秒";
        } catch (Exception e) {
            e.printStackTrace();
            return "初始化失败";
        }
    }

    /**
     * 搜索功能
     * @param model
     * @param q
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("search.html")
    public String search(Model model, String q, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "12")  int rows){
       try {
           q = new String(q.getBytes("iso-8859-1"),"utf-8");
           Map<String, Object> map = tbItemServiceImpl.selByQuery(q, page, rows);
           model.addAttribute("query",q);
//           List<TbItemChild> list = (List<TbItemChild>) map.get(("itemList"));
//           for (TbItemChild child : list) {
//               String[] images = child.getImages();
//               for (String image : images) {
//                   System.out.println(image);
//               }
//           }
           model.addAttribute("itemList",map.get("itemList"));

           model.addAttribute("totalPages",map.get("totalPages"));
           model.addAttribute("page",page);
       }catch (Exception e){
           e.printStackTrace();
       }
        return "search";
    }

    /**
     * solr新增
     * @param
     * @return
     */
    @RequestMapping("solr/add")
    @ResponseBody
    public int add(@RequestBody Map<String,Object> map){
        try {
            return tbItemServiceImpl.add((LinkedHashMap<String, Object>) map.get("item"),map.get("desc").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
