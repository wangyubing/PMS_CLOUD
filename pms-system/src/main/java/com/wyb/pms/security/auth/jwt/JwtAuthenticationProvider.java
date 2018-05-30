package com.wyb.pms.security.auth.jwt;

import com.wyb.pms.redis.RedisService;
import com.wyb.pms.security.auth.JwtAuthenticationToken;
import com.wyb.pms.security.config.JwtSettings;
import com.wyb.pms.security.model.UserContext;
import com.wyb.pms.security.model.token.RawAccessJwtToken;
import com.wyb.pms.service.LoginService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An {@link AuthenticationProvider} implementation that will use provided
 * instance of {@link} to perform authentication.
 * 
 * @author alean wang
 */
@Component
@SuppressWarnings("unchecked")
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtSettings jwtSettings;
    @Autowired
    private RedisService redisService;
    @Autowired
    private LoginService userService;
    @Autowired
    public JwtAuthenticationProvider(JwtSettings jwtSettings) {
        this.jwtSettings = jwtSettings;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RawAccessJwtToken rawAccessToken = (RawAccessJwtToken) authentication.getCredentials();

        Jws<Claims> jwsClaims = rawAccessToken.parseClaims(jwtSettings.getTokenSigningKey());
        String subject = jwsClaims.getBody().getSubject(); // 用户名
        List<GrantedAuthority> authorities = new ArrayList<>();  // 权限列表

        String tokenAuthoritySaveType = this.jwtSettings.getTokenAuthoritySaveType();
        // 从数据库中获取权限
        if ("DATABASE".equalsIgnoreCase(tokenAuthoritySaveType)) {
            Map userMap = userService.loadUserByUsername(subject);
            if (userMap != null) {
                List<String> roleList = (List<String>) userMap.get("roles");
                List<String> authrityList = (List<String>) userMap.get("authorities");
                authorities = authrityList.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
            }
        // 从token中获取权限
        }else if ("TOKEN".equalsIgnoreCase(tokenAuthoritySaveType)) {
            List<String> scopes = jwsClaims.getBody().get("scopes", List.class);
            authorities = scopes.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        // 从redis缓存中获取权限
        }else if ("REDIS".equalsIgnoreCase(tokenAuthoritySaveType)) {
            Object members = redisService.setMembers(rawAccessToken.getToken().toString()+":authrity");
            if (members != null) {
                List<String> memberList = new ArrayList<String>((LinkedHashSet) members);
                if (memberList != null) {
                    authorities = memberList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                }
            }
        }

        UserContext context = UserContext.create(subject, null);
        context.setUserId(String.valueOf(jwsClaims.getBody().get("user_id")));
        context.setCompanyId(String.valueOf(jwsClaims.getBody().get("company_id")));

        return new JwtAuthenticationToken(context, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
