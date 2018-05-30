package com.wyb.pms.config.db;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DuridConfig {
    @Bean
    public ServletRegistrationBean statViewService() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/durid/*");
        servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
        servletRegistrationBean.addInitParameter("deny","");
        servletRegistrationBean.addInitParameter("loginUsername", "durid");
        servletRegistrationBean.addInitParameter("loginPassword", "123456");
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }
    @Bean
    public FilterRegistrationBean statFilter(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions","*.js,*.css,*.gif,*.jpg,*.png,*.ico,/durid/*");
        return filterRegistrationBean;
    }
}
