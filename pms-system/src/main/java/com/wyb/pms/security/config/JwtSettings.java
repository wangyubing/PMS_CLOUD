package com.wyb.pms.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "wyb.security.jwt")
public class JwtSettings {
    /**
     * {@link } will expire after this time.
     */
    private Integer tokenExpirationTime;

    /**
     * Token issuer.
     */
    private String tokenIssuer;
    
    /**
     * Key is used to sign {@link JwtToken}.
     */
    private String tokenSigningKey;
    
    /**
     * {@link JwtToken} can be refreshed during this timeframe.
     */
    private Integer refreshTokenExpTime;

    /**
     * 获取token权限方式，REDIS 从redis服务器中获取权限，token 从token中获取权限， database 从数据库中获取权限
     * REDIS TOKEN DATABASE
     */
    private String tokenAuthoritySaveType;
    
    public Integer getRefreshTokenExpTime() {
        return refreshTokenExpTime;
    }

    public void setRefreshTokenExpTime(Integer refreshTokenExpTime) {
        this.refreshTokenExpTime = refreshTokenExpTime;
    }

    public Integer getTokenExpirationTime() {
        return tokenExpirationTime;
    }
    
    public void setTokenExpirationTime(Integer tokenExpirationTime) {
        this.tokenExpirationTime = tokenExpirationTime;
    }
    
    public String getTokenIssuer() {
        return tokenIssuer;
    }
    public void setTokenIssuer(String tokenIssuer) {
        this.tokenIssuer = tokenIssuer;
    }
    
    public String getTokenSigningKey() {
        return tokenSigningKey;
    }
    
    public void setTokenSigningKey(String tokenSigningKey) {
        this.tokenSigningKey = tokenSigningKey;
    }

    public String getTokenAuthoritySaveType() {
        return tokenAuthoritySaveType;
    }

    public void setTokenAuthoritySaveType(String tokenAuthoritySaveType) {
        this.tokenAuthoritySaveType = tokenAuthoritySaveType;
    }
}
