package com.ego.passport.controller;

import com.ego.commons.pojo.EgoResult;
import com.ego.passport.service.TbUserService;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbUser;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class TbUserController {

    @Resource
    private TbUserService tbUserServiceImpl;

    /**
     * 显示登录页面
     * @return
     */
    @RequestMapping("user/showLogin")
    public String showLogin(@RequestHeader(value = "Referer",defaultValue = "") String url,Model model,String interurl){
        if (interurl!=null&&!interurl.equals("")){
            model.addAttribute("redirect",interurl);
        }else if (!url.equals("")){
            model.addAttribute("redirect",url);
        }
        return "login";
}

    /**
     * 登录
     * @param user
     * @return
     */
    @RequestMapping("user/login")
    @ResponseBody
    public EgoResult login(TbUser user,HttpServletRequest request, HttpServletResponse response){
        return tbUserServiceImpl.login(user,request,response);
    }

    /**
     * 跳转注册页面
     * @return
     */
    @RequestMapping("user/showRegister")
    public String showRegister(){

        return "register";
    }


    /**
     * 校验用户名
     * @param username
     * @return
     */
    @RequestMapping("/user/check/{username}/1")
    @ResponseBody
    public EgoResult userCheckUsername(@PathVariable String username){
             TbUser user = new TbUser();
             user.setUsername(username);
        return tbUserServiceImpl.selByUsername(user);
    }

    /**
     * 校验手机号是否已注册
     * @param phone
     * @return
     */
    @RequestMapping("/user/check/{phone}/2")
    @ResponseBody
    public EgoResult userCheckPhone(@PathVariable String phone){
        TbUser user = new TbUser();
        user.setPhone(phone);
        return tbUserServiceImpl.selByPhone(user);
    }

    /**
     * 注册用户
     * @param user
     * @return
     */
    @RequestMapping("/user/register")
    @ResponseBody
    public EgoResult userRegister(TbUser user){
        return tbUserServiceImpl.insTbUser(user);
    }




    /**
     * 通过token获取用户信息
     * @param token
     * @param callback
     * @return
     */
    @RequestMapping("user/token/{token}")
    @ResponseBody
    public Object getUserInfo(@PathVariable String token, String callback){
        System.out.println(token);
        EgoResult er = tbUserServiceImpl.getUserInfoByToken(token);
        if (callback!=null&&!callback.equals("")){
            MappingJacksonValue mjv = new MappingJacksonValue(er);
            mjv.setJsonpFunction(callback);
            return mjv;
        }
        return er;
    }

    /**
     * 退出
     * @param token
     * @param callback
     * @return
     */
    @RequestMapping("user/logout/{token}")
    @ResponseBody
    public Object logout(@PathVariable String token, String callback,HttpServletRequest request,HttpServletResponse response){
        EgoResult er = tbUserServiceImpl.logout(token,request,response);
        if (callback!=null&&!callback.equals("")){
            MappingJacksonValue mjv = new MappingJacksonValue(er);
            mjv.setJsonpFunction(callback);
            return mjv;
        }
        return er;
    }


}
