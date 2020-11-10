package com.ego.passport.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.utils.CookieUtils;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbUserDubboService;
import com.ego.passport.service.TbUserService;
import com.ego.pojo.TbUser;
import com.ego.redis.dao.JedisDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;

@Service
public class TbUserServiceImpl implements TbUserService {

    @Reference
    private TbUserDubboService tbUserDubboServiceImpl;

    @Resource
    private JedisDao jedisDaoImpl;

    @Value("user.key")
    private String userKey;

    @Override
    public EgoResult login(TbUser user, HttpServletRequest request, HttpServletResponse response) {
        TbUser tbUser = new TbUser();
        EgoResult egoResult = new EgoResult();
        //用户登录先判断是否在 redis中
         String uKey = userKey+user.getUsername();
        if (jedisDaoImpl.exists(uKey)){
            //如果存在 则直接从redis中取
            String json = jedisDaoImpl.get(uKey);
            if (json!=null && json.equals("")){
                tbUser  = JsonUtils.jsonToPojo(json, TbUser.class);
            }
        }else {
            //如果不存在 则从mysql中取 并且最后存到redis中
           tbUser = tbUserDubboServiceImpl.selByUser(user);
           jedisDaoImpl.set(uKey,JsonUtils.objectToJson(tbUser));
        }
        if (tbUser!=null){
            egoResult.setStatus(200);
            //当用户登录成功后把用户信息放入到redis中
            String key =  UUID.randomUUID().toString();
            jedisDaoImpl.set(key,JsonUtils.objectToJson(tbUser));
            jedisDaoImpl.expire(key,60*60*24*7);
            //产生Cookie
            CookieUtils.setCookie(request,response,"TT_TOKEN",key,60*60*24*7);

        }else{
            egoResult.setMsg("用户名和密码错误");
        }
        return egoResult;
        }

    @Override
    public EgoResult getUserInfoByToken(String token) {
        EgoResult egoResult = new EgoResult();
        String json = jedisDaoImpl.get(token);
        if (json!=null&&!json.equals("")) {
            TbUser tbUser = JsonUtils.jsonToPojo(json, TbUser.class);
            //可以把密码清空
            tbUser.setPassword(null);
            egoResult.setStatus(200);
            egoResult.setMsg("OK");
            egoResult.setData(tbUser);
        }else {
            egoResult.setMsg("获取失败");
        }
        return egoResult;
    }

    @Override
    public EgoResult logout(String token, HttpServletRequest request, HttpServletResponse response) {
       EgoResult egoResult = new EgoResult();
        Long index = jedisDaoImpl.del(token);
        CookieUtils.deleteCookie(request,response,"TT_TOKEN");
        egoResult.setStatus(200);
        egoResult.setMsg("OK");
        return egoResult;
    }

    @Override
    public EgoResult selByUsername(TbUser user) {
        EgoResult result = new EgoResult();
        int index = tbUserDubboServiceImpl.selByUsername(user);
        if (index==0){
            result.setStatus(200);
            result.setData(true);
        }
        return result;
    }

    @Override
    public EgoResult selByPhone(TbUser user) {
        EgoResult result = new EgoResult();
        int index = tbUserDubboServiceImpl.selByPhone(user);
        if (index == 0){
            result.setData(true);
        }
        return result;
    }

    @Override
    public EgoResult insTbUser(TbUser user) {
        String key = userKey+user.getUsername();
        Date date = new Date();
        user.setCreated(date);
        user.setUpdated(date);
        EgoResult result = new EgoResult();
        int index = tbUserDubboServiceImpl.insTbUser(user);
        if (index>0){
            //注册成功后把用户信息存入到redis中
            if (jedisDaoImpl.exists(key)){
                //如果已存在key则不做任何操作
            }else {
                //如果不存在 则存到redis中
                jedisDaoImpl.set(key,JsonUtils.objectToJson(user));
            }
            result.setStatus(200);
        }
        return result;
    }
}
