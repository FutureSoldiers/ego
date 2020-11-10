package com.ego.dubbo.service;

import com.ego.pojo.TbUser;

public interface TbUserDubboService {
    /**
     * 根据用户名和密码查询登录
     * @param user
     * @return
     */
    TbUser selByUser(TbUser user);

    /**
     * 查询用户名是否存在
     * @param user
     * @return
     */
    int selByUsername(TbUser user);

    /**
     * 查询手机号是否已注册
     * @param user
     * @return
     */
    int selByPhone(TbUser user);

    /**
     * 用户注册
     * @param user
     * @return
     */
    int insTbUser(TbUser user);

}
