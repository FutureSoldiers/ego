package com.ego.passport.service;

import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TbUserService {

    /**
     * 登录
     * @param user
     * @return
     */
    EgoResult login(TbUser user, HttpServletRequest request, HttpServletResponse response);

    /**
     * 根据token查询用户信息
     * @param token
     * @return
     */
    EgoResult getUserInfoByToken(String token);

    /**
     * 退出
     * @param token
     * @param request
     * @param response
     * @return
     */
    EgoResult logout(String token, HttpServletRequest request, HttpServletResponse response);

    /**
     * 判断username是否存在
     * @param user
     * @return
     */
    EgoResult selByUsername(TbUser user);

    /**
     * 判断手机号是否已注册
     * @param user
     * @return
     */
    EgoResult selByPhone(TbUser user);

    /**
     * 用户注册
     * @param user
     * @return
     */
    EgoResult insTbUser(TbUser user);
}
