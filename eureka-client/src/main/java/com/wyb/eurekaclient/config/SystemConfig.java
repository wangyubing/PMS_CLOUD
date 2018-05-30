package com.wyb.eurekaclient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 超级管理员配置文件
 * Created by pc on 2018/5/29.
 */
@RefreshScope
@Component
public class SystemConfig {

    public static String username;

    public static String password;

    public String getUsername() {
        return username;
    }

    @Value("${system.superuser.username}")
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    @Value("${system.superuser.pwd}")
    public void setPassword(String password) {
        this.password = password;
    }
}
