package com.wyb.eurekaclient.test;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    private HttpServletRequest request;

    @Value("${server.port}")
    String port;

    @ResponseBody
    @RequestMapping("/url")
    public String testUrl(@RequestParam Map<String, String> params) {
        System.out.println(request.getRequestURL());
        return "=======> hi i am from port:" + port;
    }
}
