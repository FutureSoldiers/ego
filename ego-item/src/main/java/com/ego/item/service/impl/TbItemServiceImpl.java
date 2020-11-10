package com.ego.item.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.TbItemChild;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.item.service.TbItemService;
import com.ego.pojo.TbItem;
import com.ego.redis.dao.JedisDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;

@Service
public class TbItemServiceImpl implements TbItemService {

    @Reference
    private TbItemDubboService tbItemDubboServiceImpl;

    @Value("redis.item.key")
    private String itemKey;

    @Resource
    private JedisDao jedisDaoImpl;
    @Override
    public TbItemChild show(long id) {
        //先判断redis中是否存在指定key,值不为null并且不为空字符串
        String key = itemKey+id;
        if (jedisDaoImpl.exists(key)) {
            //redis中有指定key 则从redis中取 直接返回
            String json = jedisDaoImpl.get(key);
            if (json != null && !json.equals("")) {
                return JsonUtils.jsonToPojo(json, TbItemChild.class);
            }
        }
        //没有则从mysql中取 取完把数据存到redis中 然后返回结果
        TbItem tbItem = tbItemDubboServiceImpl.selById(id);
        TbItemChild itemChild = new TbItemChild();
        itemChild.setId(tbItem.getId());
        itemChild.setTitle(tbItem.getTitle());
        itemChild.setPrice(tbItem.getPrice());
        itemChild.setSellPoint(tbItem.getSellPoint());
        itemChild.setImages(tbItem.getImage()!=null&&!tbItem.getImage().equals("")?tbItem.getImage().split(","):new String[1]);
       //存到redis中
            jedisDaoImpl.set(key,JsonUtils.objectToJson(itemChild));
        return itemChild;
    }


}
