package com.ego.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.cart.service.CartService;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.pojo.TbItemChild;
import com.ego.commons.utils.CookieUtils;
import com.ego.commons.utils.HttpClientUtil;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbUser;
import com.ego.redis.dao.JedisDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Resource
    private JedisDao jedisDaoImpl;

    @Reference
    private TbItemDubboService tbItemDubboServiceImpl;

    @Value("${passport.url}")
    private String passportUrl;

    @Value("${cart.key}")
    private String cartKey;

    @Override
    public void addCart(long id, int num, HttpServletRequest request) {
        //集合中存放所有购物车商品
        List<TbItemChild> list = new ArrayList<>();
        //
        String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
        String jsonUser = HttpClientUtil.doPost(passportUrl + token);
        EgoResult er = JsonUtils.jsonToPojo(jsonUser, EgoResult.class);
        //redis 中的key
        String key = cartKey+((LinkedHashMap)er.getData()).get("username");

        //如果redis中存在key
        if (jedisDaoImpl.exists(key)){
            String json = jedisDaoImpl.get(key);
            if (json!=null&&!json.equals("")){
                list = JsonUtils.jsonToList(json, TbItemChild.class);
                for (TbItemChild tbItemChild : list) {
                    if (tbItemChild.getId()==id){
                        //购物车中存在该商品
                        tbItemChild.setNum(tbItemChild.getNum()+num);
                        jedisDaoImpl.set(key,JsonUtils.objectToJson(list));
                        return ;
                    }
                }
                //重新添加在redis中
            }
        }
        //redis中key对应value是null或""或不存在
        //购物车中没有这个商品
        TbItem item = tbItemDubboServiceImpl.selById(id);
        TbItemChild tbItemChild = new TbItemChild();

        tbItemChild.setId(item.getId());
        tbItemChild.setTitle(item.getTitle());
        tbItemChild.setImages(item.getImage()==null||item.getImage().equals("")?new String[1]:item.getImage().split(","));
        tbItemChild.setNum(num);
        tbItemChild.setPrice(item.getPrice());

        list.add(tbItemChild);

        jedisDaoImpl.set(key,JsonUtils.objectToJson(list));




        //可以存放多个商品信息.value 应该是一个集合
        //如果商品已经存在修改数量
        //如果商品没有存在,把商品对象放入到集合中


    }

    @Override
    public List<TbItemChild> showCart(HttpServletRequest request) {
        //redis 中的key
        String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
        String jsonUser = HttpClientUtil.doPost(passportUrl + token);
        EgoResult er = JsonUtils.jsonToPojo(jsonUser, EgoResult.class);
        String key = cartKey+((LinkedHashMap)er.getData()).get("username");

        String json = jedisDaoImpl.get(key);
        return JsonUtils.jsonToList(json,TbItemChild.class);
    }

    @Override
    public EgoResult update(long id, int num,HttpServletRequest request) {
        //redis 中的key
        String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
        String jsonUser = HttpClientUtil.doPost(passportUrl + token);
        EgoResult er = JsonUtils.jsonToPojo(jsonUser, EgoResult.class);
        String key = cartKey+((LinkedHashMap)er.getData()).get("username");

        String json = jedisDaoImpl.get(key);
        List<TbItemChild> list = JsonUtils.jsonToList(json, TbItemChild.class);
        for (TbItemChild child : list) {
            if (child.getId()==id){
                child.setNum(num);
            }
        }
        String ok = jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
        EgoResult egoResult = new EgoResult();
        if (ok.equals("OK")){
            egoResult.setStatus(200);
        }
        return egoResult;
    }

    @Override
    public EgoResult delete(long id, HttpServletRequest request) {
        //redis 中的key
        String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
        String jsonUser = HttpClientUtil.doPost(passportUrl + token);
        EgoResult er = JsonUtils.jsonToPojo(jsonUser, EgoResult.class);
        String key = cartKey+((LinkedHashMap)er.getData()).get("username");

        String json = jedisDaoImpl.get(key);
        List<TbItemChild> list = JsonUtils.jsonToList(json, TbItemChild.class);
        TbItemChild tbItemChild = null;
        for (TbItemChild child : list) {
            if (child.getId()==id){
                tbItemChild = child;
            }
        }
        list.remove(tbItemChild);
        String ok = jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
        EgoResult egoResult = new EgoResult();
        if (ok.equals("OK")){
            egoResult.setStatus(200);
        }
        return egoResult;
    }
}
