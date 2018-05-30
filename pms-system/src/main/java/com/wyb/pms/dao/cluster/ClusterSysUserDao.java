package com.wyb.pms.dao.cluster;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
public interface ClusterSysUserDao {

    void addUser(Map params);
}
