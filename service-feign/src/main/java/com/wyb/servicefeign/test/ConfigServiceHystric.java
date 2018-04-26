package com.wyb.servicefeign.test;

import org.springframework.stereotype.Component;

@Component
public class ConfigServiceHystric implements ConfigService {

    @Override
    public String getConfig(){
        return "error config";
    }
}
