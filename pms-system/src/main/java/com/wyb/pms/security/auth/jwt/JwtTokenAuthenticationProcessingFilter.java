package com.wyb.pms.security.auth.jwt;

import com.wyb.pms.config.security.WebSecurityConfig;
import com.wyb.pms.security.auth.JwtAuthenticationToken;
import com.wyb.pms.security.auth.jwt.extractor.TokenExtractor;
import com.wyb.pms.security.exceptions.AuthMethodNotAccessException;
import com.wyb.pms.security.model.token.RawAccessJwtToken;
import com.wyb.pms.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Performs validation of provided JWT Token.
 *
 * @author alean wang
 */
public class JwtTokenAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private final AuthenticationSuccessHandler sucessHandler;
    private final AuthenticationFailureHandler failureHandler;
    private final TokenExtractor tokenExtractor;

    
    @Autowired
    public JwtTokenAuthenticationProcessingFilter(AuthenticationSuccessHandler sucessHandler, AuthenticationFailureHandler failureHandler,
            TokenExtractor tokenExtractor, RequestMatcher matcher) {
        super(matcher);
        this.sucessHandler = sucessHandler;
        this.failureHandler = failureHandler;
        this.tokenExtractor = tokenExtractor;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        String tokenPayload = request.getHeader(WebSecurityConfig.AUTHENTICATION_HEADER_NAME);
        RawAccessJwtToken token = new RawAccessJwtToken(tokenExtractor.extract(tokenPayload));
        return getAuthenticationManager().authenticate(new JwtAuthenticationToken(token));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        this.sucessHandler.onAuthenticationSuccess(request, response, authResult);


        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }

}
