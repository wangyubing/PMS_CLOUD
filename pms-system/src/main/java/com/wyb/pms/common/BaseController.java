package com.wyb.pms.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class BaseController {

    @Autowired
    private HttpServletRequest request;

    /**
     * 获取用户ID
     * @return
     */
    public String getUserId(){
        return this.getRequestAttribute("user_id");
    }

    /**
     * 获取企业ID
     * @return
     */
    public String getCompanyId(){
        return this.getRequestAttribute("company_id");
    }

    /**
     * 设置用户ID和企业ID到参数中
     * @param params
     * @return
     */
    public void setParams(Map params){
        params.put("user_id", getUserId());
        params.put("company_id", getCompanyId());
    }

    private String getRequestAttribute(String key){
        Object object = request.getAttribute(key);
        if (object == null) {
            return "";
        }else {
            return object.toString();
        }
    }
}
