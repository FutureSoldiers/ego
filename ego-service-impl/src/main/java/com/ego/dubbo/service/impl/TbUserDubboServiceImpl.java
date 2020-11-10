package com.ego.dubbo.service.impl;

import com.ego.dubbo.service.TbUserDubboService;
import com.ego.mapper.TbUserMapper;
import com.ego.pojo.TbUser;
import com.ego.pojo.TbUserExample;

import javax.annotation.Resource;
import java.util.List;

public class TbUserDubboServiceImpl implements TbUserDubboService {

    @Resource
    private TbUserMapper tbUserMapper;

    @Override
    public TbUser selByUser(TbUser user) {
        TbUserExample example = new TbUserExample();
        example.createCriteria().andUsernameEqualTo(user.getUsername()).andPasswordEqualTo(user.getPassword());
        List<TbUser> list = tbUserMapper.selectByExample(example);
        if (list!=null&&list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public int selByUsername(TbUser user) {

        return tbUserMapper.selByUsernameMapper(user);
    }

    @Override
    public int selByPhone(TbUser user) {
        return tbUserMapper.selByPhoneMapper(user);
    }

    @Override
    public int insTbUser(TbUser user) {
        return  tbUserMapper.insertSelective(user);
    }
}
