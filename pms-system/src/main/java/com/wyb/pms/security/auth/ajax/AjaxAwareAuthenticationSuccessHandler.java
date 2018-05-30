package com.wyb.pms.security.auth.ajax;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyb.pms.common.result.ResultBody;
import com.wyb.pms.common.utils.MapUtils;
import com.wyb.pms.redis.RedisService;
import com.wyb.pms.security.config.JwtSettings;
import com.wyb.pms.security.model.UserContext;
import com.wyb.pms.security.model.token.JwtToken;
import com.wyb.pms.security.model.token.JwtTokenFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AjaxAwareAuthenticationSuccessHandler
 *
 * @author alean wang
 */
@Component
public class AjaxAwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final ObjectMapper mapper;
    private final JwtTokenFactory tokenFactory;
    @Autowired
    private RedisService redisService;
    @Autowired
    private JwtSettings jwtSettings;

    @Autowired
    public AjaxAwareAuthenticationSuccessHandler(final ObjectMapper mapper, final JwtTokenFactory tokenFactory) {
        this.mapper = mapper;
        this.tokenFactory = tokenFactory;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Map tokenMap = new HashMap();
        String tokenAuthoritySaveType = this.jwtSettings.getTokenAuthoritySaveType();
        if ("DATABASE".equalsIgnoreCase(tokenAuthoritySaveType)) {
            tokenMap = this.createTokenForDatabase(authentication);
        }else if ("TOKEN".equalsIgnoreCase(tokenAuthoritySaveType)) {
            tokenMap = this.createTokenForToken(authentication);
        }else if ("REDIS".equalsIgnoreCase(tokenAuthoritySaveType)) {
            tokenMap = this.createTokenForRedis(authentication);
        }

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        mapper.writeValue(response.getWriter(), new ResultBody(tokenMap));

        clearAuthenticationAttributes(request);
    }

    /**
     * Removes temporary authentication-related data which may have been stored
     * in the session during the authentication process..
     * 
     */
    protected final void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    private Map<String, String> createTokenForDatabase(Authentication authentication){
        UserContext userContext = (UserContext)authentication.getDetails();

        JwtToken accessToken = tokenFactory.createAccessJwtToken(userContext);
        JwtToken refreshToken = tokenFactory.createRefreshToken(userContext);

        Map<String, String> tokenMap = new HashMap<String, String>();
        tokenMap.put("token", accessToken.getToken());
        tokenMap.put("refreshToken", refreshToken.getToken());
        return tokenMap;
    }
    private Map<String, String> createTokenForToken(Authentication authentication){

        UserContext userContext = (UserContext)authentication.getDetails();
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();
        userContext.setAuthorities(authorities);

        JwtToken accessToken = tokenFactory.createAccessJwtToken(userContext);
        JwtToken refreshToken = tokenFactory.createRefreshToken(userContext);

        Map<String, String> tokenMap = new HashMap<String, String>();
        tokenMap.put("token", accessToken.getToken());
        tokenMap.put("refreshToken", refreshToken.getToken());
        return tokenMap;
    }
    private Map<String, String> createTokenForRedis(Authentication authentication) {

        UserContext userContext = (UserContext)authentication.getDetails();
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();

        JwtToken accessToken = tokenFactory.createAccessJwtToken(userContext);
        JwtToken refreshToken = tokenFactory.createRefreshToken(userContext);

        String tokenKey = accessToken.getToken().toString();
        if (authorities != null) {
            for (GrantedAuthority authrity : authorities) {
                redisService.add(tokenKey + ":authrity", authrity.getAuthority(), Long.valueOf(jwtSettings.getTokenExpirationTime()*60));
            }
        }

        Map<String, String> tokenMap = new HashMap<String, String>();
        tokenMap.put("token", accessToken.getToken());
        tokenMap.put("refreshToken", refreshToken.getToken());
        return tokenMap;
    }
}
