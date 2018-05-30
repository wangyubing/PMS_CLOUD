package com.wyb.pms.security.auth.ajax;

import com.wyb.pms.common.utils.MapUtils;
import com.wyb.pms.security.auth.JwtAuthenticationToken;
import com.wyb.pms.security.model.UserContext;
import com.wyb.pms.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author alean wang
 */
@Component
public class AjaxAuthenticationProvider implements AuthenticationProvider {
    private final BCryptPasswordEncoder encoder;
    private final LoginService userService;

    @Autowired
    public AjaxAuthenticationProvider(final LoginService userService, final BCryptPasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException  {
        Assert.notNull(authentication, "No authentication data provided");

        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        Map userMap = userService.loadUserByUsername(username);
        if (userMap == null) {
            throw new BadCredentialsException("Authentication Failed. user not exists.");
        }
        BCryptPasswordEncoder pwdEncode = new BCryptPasswordEncoder();
        if (!encoder.matches(password, pwdEncode.encode(MapUtils.getString(userMap, "user_pwd")))) {
            throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
        }


        List<String> roleList = (List<String>) userMap.get("roles");
        List<String> authrityList = (List<String>) userMap.get("authorities");

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (authrityList != null) {
            authorities = authrityList.stream()
                    .map(authority -> new SimpleGrantedAuthority(authority))
                    .collect(Collectors.toList());
        }
        UserContext userContext = UserContext.create(MapUtils.getString(userMap, "user_name"), null);
        userContext.setUserId(MapUtils.getString(userMap, "user_id"));
        userContext.setUserId(MapUtils.getString(userMap, "company_id"));
        //UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
        //usernamePasswordAuthenticationToken.setDetails(MapUtils.build(""));
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(userContext, authorities);
        return jwtAuthenticationToken;//new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
