package com.ego.order.controller;

import com.ego.commons.pojo.EgoResult;
import com.ego.commons.pojo.TbItemChild;
import com.ego.order.pojo.MyOrderParam;
import com.ego.order.service.TbOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class OrderController {

    @Resource
    private TbOrderService tbOrderServiceImpl;

    /**
     * 显示订单确认页面
     * @param model
     * @param ids
     * @param request
     * @return
     */
    @RequestMapping("order/order-cart.html")
    public String showCartOrder(Model model, @RequestParam("id") List<Long> ids, HttpServletRequest request){
        List<TbItemChild> list = tbOrderServiceImpl.showOrderCart(ids, request);
        if (list!=null&&list.size()>0){
            for (TbItemChild child : list) {
                System.out.println( child.getId());
                System.out.println(child.getTitle());
                System.out.println(child.getNum().toString());

            }
            model.addAttribute("cartList",list);
            return "order-cart";
        }
        System.out.println("list为空");
        return "order-cart";

    }

    @RequestMapping("order/create.html")
    public String createOrder(MyOrderParam param,HttpServletRequest request){
        EgoResult er = tbOrderServiceImpl.create(param, request);
        if (er.getStatus()==200){
            return "my-orders";
        }else {
            request.setAttribute("message","订单创建失败");
            return "error/exception";
        }
    }


}
