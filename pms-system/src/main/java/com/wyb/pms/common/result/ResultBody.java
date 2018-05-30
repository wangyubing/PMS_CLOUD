package com.wyb.pms.common.result;

/**
 * 返回体
 *
 * Created by bysocket on 14/03/2017.
 */
public class ResultBody {
    /**
     * 响应代码
     */
    private String code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应结果
     */
    private Object result;

    public ResultBody() {
        this.code = GlobalErrorInfoEnum.SUCCESS.getCode();
        this.message = "";
    }

    public ResultBody(ErrorInfoInterface errorInfo) {
        this.code = errorInfo.getCode();
        this.message = errorInfo.getMessage();
    }

    public ResultBody(Object result) {
        this.code = GlobalErrorInfoEnum.SUCCESS.getCode();
        this.message = GlobalErrorInfoEnum.SUCCESS.getMessage();
        this.result = result;
    }

    public ResultBody(ErrorInfoInterface errorInfo, String errMsg) {
        this.code = errorInfo.getCode();
        this.message = errMsg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public static ResultBody sucess(Object obj) {
        return new ResultBody(obj);
    }
    public static ResultBody error(String errorMsg) {
        return new ResultBody(GlobalErrorInfoEnum.ERROR_SERVER, errorMsg);
    }
    public static ResultBody error(ErrorInfoInterface errorInfoInterface, String errorMsg) {
        return new ResultBody(errorInfoInterface, errorMsg);
    }
    public static ResultBody error(ErrorInfoInterface errorInfoInterface) {
        return new ResultBody(errorInfoInterface, errorInfoInterface.getMessage());
    }
}
