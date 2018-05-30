package com.wyb.pms.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;


public class SysUser {
    @Max(value = 50, message = "ID not max 50")
    String user_pwd ;

    @NotEmpty(message = "不能伪娘控")
    @Max(value = 20, message = "max 20")
    String user_name ;
}
