package com.wiceflow.login.controller;

import com.wiceflow.login.eneity.TUser;
import com.wiceflow.login.service.UserService;
import com.wiceflow.login.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author BF
 * @date 2018/1/19
 * 登录控制器
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 用户登录
     *
     * @param user     一定要对应封装数据 count password
     * @param request  获取session
     * @param response 返回cookie
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult login(TUser user, HttpServletRequest request, HttpServletResponse response){
        return userService.checkLogin(user,request,response);
    }

    /**
     * 退出登录
     * @param request   获取Application
     * @param response  清除cookie后并返回
     *                  这里的退出登录会将cookie和application都清除掉
     *                  TODO 注意 这个方法也是监控浏览器关闭后清除application的方法
     */
    @RequestMapping(value = "/signOut",method = RequestMethod.GET)
    public void signOutLogin(HttpServletRequest request, HttpServletResponse response){
        userService.signOutLogin(request,response);
    }

    /**
     * 检查该账号是否被使用
     * @param count
     * @return 若账号已被注册则由前端控制注册按钮不可点
     */
    @PostMapping(value = "/checkCount")
    @ResponseBody
    public AjaxResult checkCount(@RequestParam("count") String count){
        return userService.checkCount(count);
    }


    /**
     * 注册账号
     * @param user 封装了前台传过来的用户填写信息
     * @return
     */
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult register(TUser user){
        return userService.register(user);
    }
}
