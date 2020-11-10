package com.ego.cart.interceptor;

import com.ego.commons.pojo.EgoResult;
import com.ego.commons.utils.CookieUtils;
import com.ego.commons.utils.HttpClientUtil;
import com.ego.commons.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
        if (token!=null&&!token.equals("")){
            String json = HttpClientUtil.doPost("http://localhost:8084/user/token/" + token);
            EgoResult er = JsonUtils.jsonToPojo(json, EgoResult.class);
            if (er.getStatus()==200){
                return true;
            }
        }
        String num = request.getParameter("num");
        response.sendRedirect("http://localhost:8084/user/showLogin?interurl"+request.getRequestURL()+"%3Fnum="+num);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
