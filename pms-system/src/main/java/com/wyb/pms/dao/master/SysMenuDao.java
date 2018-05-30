package com.wyb.pms.dao.master;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysMenuDao {

    List<Map> queryAllAuthority();
}
