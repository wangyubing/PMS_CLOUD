package com.wyb.eurekaclient.test;

import com.wyb.eurekaclient.config.SystemConfig;
import com.wyb.test.ITestCommonService;
import com.wyb.test.TestCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RefreshScope
public class TestController {

    @Autowired
    private HttpServletRequest request;


    @Autowired
    private ITestCommonService testCommonService ;

    @ResponseBody
    @RequestMapping("/url")
    public String testUrl(@RequestParam Map<String, String> params) {
        String name = params.get("name");
        String message = testCommonService.getMessage(name);
        System.out.println(request.getRequestURL());
        return "=======> this is info:" + "  message: " + message + " username:" + SystemConfig.username + " password:" + SystemConfig.password;
    }
}
