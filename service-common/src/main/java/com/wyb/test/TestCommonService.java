package com.wyb.test;

import org.springframework.stereotype.Service;

/**
 * Created by pc on 2018/5/7.
 */
@Service
public class TestCommonService implements ITestCommonService {

    public String getMessage(String name){

        return "hello " + name;
    }
}
