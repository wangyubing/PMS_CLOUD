package com.wyb.pms.controller;

import com.wyb.pms.common.BaseController;
import com.wyb.pms.common.result.ResultBody;
import com.wyb.pms.common.utils.MapUtils;
import com.wyb.pms.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class SysUserController extends BaseController {
    @Autowired
    SysUserService sysUserService;

    @RequestMapping(value = "/query-users", method = RequestMethod.GET)
    public ResultBody queryUserList(@RequestParam Map params) {

        return ResultBody.sucess(sysUserService.queryUserList());
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ResultBody detail(@RequestParam Map params) {
        return ResultBody.sucess("user detail");
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ResultBody add(@RequestParam Map params) {

        sysUserService.addUser(params);
        return ResultBody.sucess("user add");
    }
}
