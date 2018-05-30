package com.wyb.pms.common.result.constant;


import com.wyb.pms.common.result.ErrorInfoInterface;

/**
 * 登录错误码 案例
 *
 * Created by bysocket on 14/03/2017.
 */
public enum LoginErrorInfoEnum implements ErrorInfoInterface {
    ERROR_USER_PWD("101","用户名或密码错误"),
    ERROR_USER_UNFIND("102","用户未找到"),
    ERROR_AUTH_UNAUTHORIZED("401","身份验证失败"),
    ERROR_AUTH_FORBIDDEN("403","无访问权限"),
    ERROR_TOKEN_INVALID("410","token无效"),
    ERROR_TOKEN_EXPIRE("411","token超时");

    private String code;

    private String message;

    LoginErrorInfoEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode(){
        return this.code;
    }

    public String getMessage(){
        return this.message;
    }
}
