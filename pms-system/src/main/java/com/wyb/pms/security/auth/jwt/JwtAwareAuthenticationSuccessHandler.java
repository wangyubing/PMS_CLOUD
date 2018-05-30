package com.wyb.pms.security.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyb.pms.common.result.GlobalErrorInfoException;
import com.wyb.pms.common.result.ResultBody;
import com.wyb.pms.common.result.constant.LoginErrorInfoEnum;
import com.wyb.pms.security.exceptions.AuthMethodNotAccessException;
import com.wyb.pms.security.model.UserContext;
import com.wyb.pms.security.model.token.JwtToken;
import com.wyb.pms.security.model.token.JwtTokenFactory;
import com.wyb.pms.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AjaxAwareAuthenticationSuccessHandler
 *
 * @author alean wang
 */
@Component
public class JwtAwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final ObjectMapper mapper;
    private final JwtTokenFactory tokenFactory;

    @Autowired
    LoginService loginService;

    @Autowired
    public JwtAwareAuthenticationSuccessHandler(final ObjectMapper mapper, final JwtTokenFactory tokenFactory) {
        this.mapper = mapper;
        this.tokenFactory = tokenFactory;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();
        UserContext userContext = (UserContext) authentication.getDetails();
        //UserContext userContext = (UserContext) authentication.getPrincipal();
        boolean isAuthenticated = authentication.isAuthenticated();

        String requestPath = request.getServletPath();

        if (!validAuthority(requestPath, authorities)) {
            SecurityContextHolder.clearContext();
            mapper.writeValue(response.getWriter(), ResultBody.error(LoginErrorInfoEnum.ERROR_AUTH_FORBIDDEN));
        }else {
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            if (userContext != null) {
                request.setAttribute("user_id", userContext.getUserId());
                request.setAttribute("company_id", userContext.getCompanyId());
            }
        }
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


    private boolean validAuthority(String requestPath, List<GrantedAuthority> authorities){

        Map<String, String> allAuthorityMap = loginService.queryAllAuthority();

        String authorityKey = allAuthorityMap.get(requestPath);
        if (authorityKey != null) {
            for (GrantedAuthority ga : authorities) {
                if (authorityKey.equals(ga.getAuthority())) {
                    return true;
                }
            }
        }
        return false;
    }
}
