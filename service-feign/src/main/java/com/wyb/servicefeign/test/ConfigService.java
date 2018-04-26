package com.wyb.servicefeign.test;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by pc on 2018/4/26.
 */

@FeignClient(value = "config-client", fallback = ConfigServiceHystric.class)
public interface ConfigService {
    @RequestMapping(value = "/api/config",method = RequestMethod.GET)
    String getConfig();
}
