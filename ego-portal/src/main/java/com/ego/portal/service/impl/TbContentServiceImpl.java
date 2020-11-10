package com.ego.portal.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbContentDubboService;
import com.ego.pojo.TbContent;
import com.ego.portal.service.TbContentService;
import com.ego.redis.dao.JedisDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class TbContentServiceImpl implements TbContentService {

    @Reference
  private TbContentDubboService tbContentDubboServiceImpl;

    @Resource
    private JedisDao jedisDaoImpl;

    @Value("${redis.bigpic.key}")
    private String key;

    @Override
    public String showBigPic() {
        //先判断在redis中是否存在
        if (jedisDaoImpl.exists(key)){
            String value = jedisDaoImpl.get(key);
            if (value!=null&&!value.equals("")){
                System.out.println(value);
                return value;
            }
        }
        //如果不存在从mysql中取,取完后放入到redis中
        List<TbContent> list = tbContentDubboServiceImpl.selByCount(6, true);

        List<Map<String,Object>> mapList = new ArrayList<>();
        for (TbContent content : list) {
           Map<String,Object> map = new LinkedHashMap<>();

            map.put("srcB",content.getPic2());
            map.put("height",240);
            map.put("alt","对不起,加载图片失败");
            map.put("width",670);
            map.put("src",content.getPic());
            map.put("widthB",550);
            map.put("href",content.getUrl());
            map.put("heightB",240);
            System.out.println(map.toString());
            mapList.add(map);
        }
//        for (Map<String, Object> map : mapList) {
//            System.out.println(map.toString());
//        }

        String listJson = JsonUtils.objectToJson(mapList);
//        System.out.println(listJson);
        //把数据放入到redis中
        jedisDaoImpl.set(key,listJson);
        return listJson;
    }
}
