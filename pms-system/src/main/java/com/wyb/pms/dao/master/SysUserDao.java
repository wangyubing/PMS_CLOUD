package com.wyb.pms.dao.master;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysUserDao {

    void delUser(String id);

    List<Map> queryUserList();

    Map queryUser(@Param("user_name") String username);

    List<Map> queryRolesByUserId(String userId);

    List<Map> queryAuthoritiesByUserId(String userId);

    List<Map> queryAllAuthority();

    void addUser(Map params);
}
