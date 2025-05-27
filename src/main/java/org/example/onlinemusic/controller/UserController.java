package org.example.onlinemusic.controller;

import org.example.onlinemusic.mapper.UserMapper;
import org.example.onlinemusic.model.User;
import org.example.onlinemusic.tools.Constant;
import org.example.onlinemusic.tools.ResponseBodyMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping("/login1")
    public ResponseBodyMessage<User> login1 (@RequestParam String username, @RequestParam String password,
                                             HttpServletRequest request) {

        User userLogin = new User();
        userLogin.setUsername(username);
        userLogin.setPassword(password);

        User user = userMapper.login(userLogin);

        if(user == null) {
            System.out.println("登录失败！");
            return new ResponseBodyMessage<>(-1,"登录失败！",userLogin);
        }else {
            // 将用户信息存储到session会话之中,始终存储用户的状态
            request.getSession().setAttribute(Constant.USERINFO_SESSION_KEY,user);
            return new ResponseBodyMessage<>(0,"登录成功！",userLogin);
        }
    }

    /**
     * 登录请求
     * @param username
     * @param password
     * @param request
     * @return
     */

    @RequestMapping("/login")
    public ResponseBodyMessage<User> login(@RequestParam String username, @RequestParam String password,
                                           HttpServletRequest request) {

        User user = userMapper.selectByName(username);

        if(user == null) {
            System.out.println("登录失败！");
            return new ResponseBodyMessage<>(-1,"用户名或者密码错误！",user);
        }else {// 不等于空  判断数据库中返回的密码和用户输入的密码是否相同
            boolean flg = bCryptPasswordEncoder.matches(password,user.getPassword());
            if(!flg) {
                return new ResponseBodyMessage<>(-1,"用户名或者密码错误！",user);
            }
            request.getSession().setAttribute(Constant.USERINFO_SESSION_KEY,user);
            return new ResponseBodyMessage<>(0,"登录成功！",user);
        }
    }


}
