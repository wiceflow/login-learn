package com.wiceflow.login.eneity;

import java.io.Serializable;

/**
 * @author 
 */
public class TUser implements Serializable {
    private Integer id;

    /**
     * 名字
     */
    private String userName;

    /**
     * 账号
     */
    private String userCount;

    /**
     * 密码
     */
    private String password;

    /**
     * 权限
     */
    private Integer userPower;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserCount() {
        return userCount;
    }

    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserPower() {
        return userPower;
    }

    public void setUserPower(Integer userPower) {
        this.userPower = userPower;
    }
}