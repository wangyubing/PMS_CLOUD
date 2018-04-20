package com.wyb.configclient.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RefreshScope
public class TestController {

    @Value("${config}")
    String config;

    @ResponseBody
    @RequestMapping("/config")
    public String testUrl(@RequestParam Map<String, String> params){
        System.out.println("config client return value: " + config);
        return "config client return value: " + config;
    }
}
