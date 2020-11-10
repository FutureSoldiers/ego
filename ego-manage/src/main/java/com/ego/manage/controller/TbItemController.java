package com.ego.manage.controller;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.manage.service.TbItemService;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbItemDesc;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class TbItemController {

    @Resource
    private TbItemService tbItemServiceImpl;

    /**
     * 分页显示商品
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("item/list")
    @ResponseBody
    public EasyUIDataGrid show(int page,int rows){
        EasyUIDataGrid show = tbItemServiceImpl.show(page,rows);
      return show;
    }

    /**
     * 显示商品修改
     * @return
     */
    @RequestMapping("rest/page/item-edit")
    @ResponseBody
    public String showItemEdit(){
        return "item-edit";
    }

    /**
     * 商品删除
     * @param ids
     * @return
     */
    @RequestMapping("rest/item/delete")
    @ResponseBody
    public EgoResult delete(String ids){
        EgoResult egoResult = new EgoResult();
        int index = tbItemServiceImpl.update(ids, (byte) 3);
        if (index==1){
            egoResult.setStatus(200);
        }
        return egoResult;
    }

    /**
     * 商品上架
     * @param ids
     * @return
     */
    @RequestMapping("rest/item/reshelf")
    @ResponseBody
    public EgoResult reshelf(String ids){
        EgoResult egoResult = new EgoResult();
        int index = tbItemServiceImpl.update(ids, (byte) 1);
        if (index==1){
            egoResult.setStatus(200);
        }
        return egoResult;
    }

    /**
     * 商品下架
     * @param ids
     * @return
     */
    @RequestMapping("rest/item/instock")
    public EgoResult instock(String ids){
        EgoResult egoResult = new EgoResult();
        int index = tbItemServiceImpl.update(ids, (byte) 2);
        if (index==1){
            egoResult.setStatus(200);
        }
        return egoResult;
    }

    /**
     * 商品新增
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping("item/save")
    @ResponseBody
    public EgoResult insert(TbItem item,String desc,String itemParams){
        EgoResult egoResult = new EgoResult();
        int index = 0;
        try {
            index = tbItemServiceImpl.save(item,desc,itemParams);
            if (index==1){
                egoResult.setStatus(200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            egoResult.setData(e.getMessage());
        }
        return egoResult;
    }


}
