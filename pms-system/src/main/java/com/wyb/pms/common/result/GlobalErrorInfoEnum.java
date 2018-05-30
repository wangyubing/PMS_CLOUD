package com.wyb.pms.common.result;

/**
 * 应用系统级别的错误码
 *
 */
public enum GlobalErrorInfoEnum implements ErrorInfoInterface{
    SUCCESS("0", "操作成功"),
    ERROR_SERVER("1", "服务端异常"),
    ERROR_PARAMS("2", "请求参数错误"),
    ERROR_USER_NOT_FIND("3", "用户不存在");

    private String code;

    private String message;

    GlobalErrorInfoEnum(String code, String message) {
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
