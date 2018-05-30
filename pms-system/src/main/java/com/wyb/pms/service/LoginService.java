package com.wyb.pms.service;

import com.wyb.pms.common.result.GlobalErrorInfoException;
import com.wyb.pms.common.result.constant.LoginErrorInfoEnum;
import com.wyb.pms.common.utils.MapUtils;
import com.wyb.pms.dao.master.SysMenuDao;
import com.wyb.pms.dao.master.SysUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@CacheConfig
public class LoginService {
    @Autowired
    SysUserDao sysUserDao;
    @Autowired
    SysMenuDao sysMenuDao;

    public String login(Map params) throws GlobalErrorInfoException {
        String userMobile = MapUtils.getString(params, "user_mobile");
        String userPwd = MapUtils.getString(params, "user_pwd");

        //sysUserService.queryUserList();
        if (userMobile.equals("wyb") && "123".equals(userPwd)) {
            return UUID.randomUUID().toString();
        }

        throw new GlobalErrorInfoException(LoginErrorInfoEnum.ERROR_USER_PWD);
    }

    public Map loadUserByUsername(String s) {
        Map userMap = sysUserDao.queryUser(s);
        if(userMap == null){
            throw new UsernameNotFoundException("用户名不存在");
        }
        String userId = MapUtils.getString(userMap, "user_id");

        List<String> roleList = new ArrayList<>();
        List<String> authorityList = new ArrayList<>();

        roleList.add("admin");
        authorityList.add("USER-QUERY-USERS");
        authorityList.add("USER-ADD");

        /*List<Map> roles = sysUserDao.queryRolesByUserId(userId);
        for (Map roleMap : roles) {
            roleList.add(MapUtils.getString(roleMap, "role_id"));
        }

        List<Map> authorities = sysUserDao.queryAuthoritiesByUserId(userId);
        for (Map authorityMap : authorities) {
            authorityList.add(MapUtils.getString(authorityMap, "menu_key"));
        }*/

        userMap.put("roles", roleList);
        userMap.put("authorities", authorityList);

        return userMap;
    }

    @Cacheable(cacheNames = "mysql:authority")
    public Map queryAllAuthority() {
        List<Map> authorities = sysMenuDao.queryAllAuthority();
        Map authorityMap = new HashMap();
        if (authorities != null) {
            for (Map authMap : authorities) {
                String url = MapUtils.getString(authMap, "menu_url");
                String key = MapUtils.getString(authMap, "menu_key");
                if (!"".equals(url) && !"".equals(key)) {
                    authorityMap.put(url, key);
                }
            }
        }
        return authorityMap;
    }
}
