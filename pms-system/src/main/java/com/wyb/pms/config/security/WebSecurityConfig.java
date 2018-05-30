package com.wyb.pms.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyb.pms.security.RestAuthenticationEntryPoint;
import com.wyb.pms.security.auth.ajax.AjaxAuthenticationProvider;
import com.wyb.pms.security.auth.ajax.AjaxAwareAuthenticationSuccessHandler;
import com.wyb.pms.security.auth.ajax.AjaxLoginProcessingFilter;
import com.wyb.pms.security.auth.jwt.JwtAuthenticationProvider;
import com.wyb.pms.security.auth.jwt.JwtAwareAuthenticationSuccessHandler;
import com.wyb.pms.security.auth.jwt.JwtTokenAuthenticationProcessingFilter;
import com.wyb.pms.security.auth.jwt.SkipPathRequestMatcher;
import com.wyb.pms.security.auth.jwt.extractor.TokenExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

/**
 * WebSecurityConfig
 *
 * @author vladimir.stankovic
 *
 * Aug 3, 2016
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String AUTHENTICATION_HEADER_NAME = "Authorization";
    public static final String AUTHENTICATION_URL = "/api/auth/login";
    public static final String REFRESH_TOKEN_URL = "/api/auth/token";
    public static final String API_ROOT_URL = "/api/**";

    @Autowired
    private RestAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private AjaxAwareAuthenticationSuccessHandler ajaxSuccessHandler;
    @Autowired
    private JwtAwareAuthenticationSuccessHandler jwtSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler failureHandler;
    @Autowired
    private AjaxAuthenticationProvider ajaxAuthenticationProvider;
    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;
    @Autowired
    private TokenExtractor tokenExtractor;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ObjectMapper objectMapper;

    // 实例化身份验证拦截器
    protected AjaxLoginProcessingFilter buildAjaxLoginProcessingFilter(String loginEntryPoint) throws Exception {
        AjaxLoginProcessingFilter filter = new AjaxLoginProcessingFilter(loginEntryPoint, ajaxSuccessHandler, failureHandler, objectMapper);
        filter.setAuthenticationManager(this.authenticationManager); // 拦截器加入管理器
        return filter;
    }

    // 实例化权限认证拦截器
    protected JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter(List<String> pathsToSkip, String pattern) throws Exception {
        // url路径匹配工具
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, pattern);
        JwtTokenAuthenticationProcessingFilter filter
                = new JwtTokenAuthenticationProcessingFilter(jwtSuccessHandler, failureHandler, tokenExtractor, matcher);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(ajaxAuthenticationProvider);
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<String> permitAllEndpointList = Arrays.asList(
                AUTHENTICATION_URL,
                REFRESH_TOKEN_URL,
                "/api/auth/userlogin",
                "/console"
        );

        http
                .csrf().disable() // We don't need CSRF for JWT based authentication
                .exceptionHandling()
                .authenticationEntryPoint(this.authenticationEntryPoint)

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers(permitAllEndpointList.toArray(new String[permitAllEndpointList.size()]))
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(API_ROOT_URL).authenticated() // Protected API End-points
                .and()
                .addFilterBefore(buildAjaxLoginProcessingFilter(AUTHENTICATION_URL), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(permitAllEndpointList,
                        API_ROOT_URL), UsernamePasswordAuthenticationFilter.class);

    }
}
