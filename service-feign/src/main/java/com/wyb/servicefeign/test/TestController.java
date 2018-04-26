package com.wyb.servicefeign.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    SchedualServiceHi schedualServiceHi;

    @Autowired
    ConfigService configService;

    @RequestMapping(value = "/hi")
    public String hi(@RequestParam String name){
        return schedualServiceHi.sayHiFromClientOne(name) + "    config service:" + configService.getConfig();
    }
}
