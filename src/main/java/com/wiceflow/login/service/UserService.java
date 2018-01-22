package com.wiceflow.login.service;
import com.wiceflow.login.eneity.TUser;
import com.wiceflow.login.util.AjaxResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author BF
 * @date 2018/1/19
 */
public interface UserService {

    /**
     * 检查用户登录信息
     * @param user
     * @param request
     * @param response
     * @return
     */
    AjaxResult checkLogin(TUser user, HttpServletRequest request, HttpServletResponse response);

    /**
     * 退出登录
     * @param request
     * @param response
     */
    void signOutLogin(HttpServletRequest request, HttpServletResponse response);

    /**
     * 注册
     * @param user
     * @return
     */
    AjaxResult register(TUser user);

    /**
     * 检查账号是否被注册
     * @param count
     * @return
     */
    AjaxResult checkCount(String count);
}
