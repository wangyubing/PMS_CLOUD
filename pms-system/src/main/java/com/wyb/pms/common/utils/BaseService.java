package com.wyb.pms.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

public class BaseService {

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

    private String getRequestAttribute(String key){
        Object object = request.getAttribute(key);
        if (object == null) {
            return "";
        }else {
            return object.toString();
        }
    }
}
