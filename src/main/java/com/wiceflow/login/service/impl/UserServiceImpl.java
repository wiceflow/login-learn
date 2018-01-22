package com.wiceflow.login.service.impl;

import com.wiceflow.login.dao.TUserMapper;
import com.wiceflow.login.eneity.TUser;
import com.wiceflow.login.eneity.TUserExample;
import com.wiceflow.login.service.UserService;
import com.wiceflow.login.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author BF
 * @date 2018/1/19
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    TUserMapper userMapper;

    /**
     * 用户登录
     * @param user     一定要对应封装数据 count password
     * @param request  获取session
     * @param response 返回cookie
     * @return
     */
    @Override
    public AjaxResult checkLogin(TUser user, HttpServletRequest request, HttpServletResponse response) {
        List<TUser> userList = new ArrayList<>();
        AjaxResult result = new AjaxResult();
        // 先查一遍数据库是否有该用户
        TUserExample userExample = new TUserExample();
        userExample.createCriteria().andUserCountEqualTo(user.getUserCount());
        userList = userMapper.selectByExample(userExample);
        // 查询为空 无该用户
        if (userList.size() == 0 || userList.get(0) == null) {
            result.setStatus(801);
            result.setMessage("用户账号不存在");
        } else {
            // 获取第一个用户
            TUser loginUser = userList.get(0);
            if (user.getPassword().equals(loginUser.getPassword())) {
                // 生成唯一的标志UUID  这个结果会有横杠 eg：id942cd30b-16c8-449e-8dc5-028f38495bb5
                String uuId = UUID.randomUUID().toString();
                //替换掉中间的那个斜杠
                uuId = uuId.replace("-", "");
                // 用户登录成功丢进session  这里用一个随机数控制一个账户只能在一个地方登录 Application会消耗服务器性能，不过考虑到操作人员不多就使用了。
                request.getServletContext().setAttribute(user.getUserCount(), uuId);
                // 设置Cookie 用来判断是否登录
                Cookie cookie1 = new Cookie("loginStatus", user.getUserCount());
                // 用来判断是否被顶号
                Cookie cookie2 = new Cookie(user.getUserCount(), uuId);
                // 这个要设置 表示该COOKIE在跟路径下联用
                cookie1.setPath("/");
                cookie2.setPath("/");
                // 不设置的话，则cookies不写入硬盘,而是写在内存,只在当前页面有用,以秒为单位  -1表示关闭浏览器cookie即消失 TODO 获取关闭浏览器动作清除Application
                cookie1.setMaxAge(-1);
                cookie2.setMaxAge(-1);
                response.addCookie(cookie1);
                response.addCookie(cookie2);
                result.setResult(loginUser);
            } else {
                result.setStatus(801);
                result.setMessage("密码错误");
            }
        }
        return result;
    }

    /**
     * 退出登录
     * @param request   获取Application
     * @param response  清除cookie后并返回
     *                  这里的退出登录会将cookie和application都清除掉
     *                  TODO 注意 这个方法也是监控浏览器关闭后清除application的方法
     */
    @Override
    public void signOutLogin(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        // 清除浏览器Cookie属性
        if (cookies != null) {
            String userCount = null;
            // 清除 loginStatus
            for (Cookie cookie : cookies) {
                if ("loginStatus".equals(cookie.getName())) {
                    // 获取loginStatus的值
                    userCount = cookie.getValue();
                    cookie.setValue(null);
                    // 立即销毁cookie
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
            // 若程序清除了 loginStatus 则会出现userCount不为空 这里要清除
            if (userCount!=null){
                // 清除Application
                request.getServletContext().setAttribute(userCount,"");
                request.removeAttribute(userCount);

                for (Cookie cookie : cookies){
                    // 这个属性是用来判断是否重复登录的
                    if (userCount.equals(cookie.getName())){
                        cookie.setValue(null);
                        // 立即销毁cookie
                        cookie.setMaxAge(0);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    }
                }
            }
        }
    }

    /**
     * 注册用户 这里经过检测账号已经不可能重复了
     * @param user  封装了前台传过来的用户填写信息
     * @return
     */
    @Override
    public AjaxResult register(TUser user) {
        AjaxResult result  = new AjaxResult();
        // 权限目前还没有开放，统一设置默认1
        if (user.getUserPower()==null){
            user.setUserPower(1);
        }
        // 因为返回值为int 若没有设置返回主键则返回null 所以使用try / catch捕获是否出错
        try{
            userMapper.insert(user);
            result.setMessage("注册成功");
        }catch (Exception e){
            result.setStatus(803);
            result.setMessage("注册出错~，具体错误请排查" + "\r\n" + e.getMessage());
            return result;
        }
        return result;
    }

    /**
     * 检查账号是否被注册
     * @param count 用户新建的账号
     * @return
     */
    @Override
    public AjaxResult checkCount(String count) {
        AjaxResult result = new AjaxResult();
        TUserExample userExample = new TUserExample();
        userExample.createCriteria().andUserCountEqualTo(count);
        // 如果返回的list不为空
        List<TUser> lists = userMapper.selectByExample(userExample);
        result.setResult(lists.size());
        if (lists.size()==0) {
            result.setMessage("恭喜！该账号可以注册");
        }else {
            result.setStatus(804);
            result.setMessage("账号已被注册");
        }
        return result;
    }
}
