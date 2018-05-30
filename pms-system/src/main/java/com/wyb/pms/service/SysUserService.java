package com.wyb.pms.service;

import com.wyb.pms.common.utils.BaseService;
import com.wyb.pms.dao.cluster.ClusterSysUserDao;
import com.wyb.pms.dao.master.SysUserDao;
import com.wyb.pms.security.model.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class SysUserService extends BaseService{
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private ClusterSysUserDao clusterSysUserDao;

    public List<Map> queryUserList(){
        System.out.println(this.getUserId());
        return sysUserDao.queryUserList();
    }

    @Transactional
    public void addUser(Map params) {
        clusterSysUserDao.addUser(params);
        sysUserDao.addUser(params);
        throw new RuntimeException("跑出异常");
    }
}
