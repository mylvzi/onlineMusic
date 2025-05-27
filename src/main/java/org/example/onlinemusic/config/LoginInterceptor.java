package org.example.onlinemusic.config;

import org.example.onlinemusic.tools.Constant;
import org.example.onlinemusic.tools.ResponseBodyMessage;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession httpSession = request.getSession(false);
        if(httpSession != null && httpSession.getAttribute(Constant.USERINFO_SESSION_KEY) != null) {
            return true;
        }
        System.out.println("没有登录！");
        response.sendRedirect("/login.html"); // 设置自动跳转
        return false;
    }
}
