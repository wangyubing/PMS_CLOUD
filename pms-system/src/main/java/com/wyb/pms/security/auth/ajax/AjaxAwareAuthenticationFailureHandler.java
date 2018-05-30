package com.wyb.pms.security.auth.ajax;

import com.alibaba.druid.wall.violation.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyb.pms.common.result.ResultBody;
import com.wyb.pms.common.result.constant.LoginErrorInfoEnum;
import com.wyb.pms.security.exceptions.AuthMethodNotAccessException;
import com.wyb.pms.security.exceptions.AuthMethodNotSupportedException;
import com.wyb.pms.security.exceptions.JwtExpiredTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author alean wang
 */
@Component
public class AjaxAwareAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper mapper;
    
    @Autowired
    public AjaxAwareAuthenticationFailureHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }	
    
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("utf-8");
		//response.sendError(HttpStatus.FORBIDDEN.value(),"fuck you");
		if (e instanceof BadCredentialsException) {
			mapper.writeValue(response.getWriter(), ResultBody.error(LoginErrorInfoEnum.ERROR_AUTH_UNAUTHORIZED));
		} else if (e instanceof JwtExpiredTokenException) {
			mapper.writeValue(response.getWriter(), ResultBody.error(LoginErrorInfoEnum.ERROR_TOKEN_EXPIRE));
		} else if (e instanceof AuthMethodNotSupportedException) {
		    mapper.writeValue(response.getWriter(),ResultBody.error(LoginErrorInfoEnum.ERROR_AUTH_UNAUTHORIZED, e.getMessage()));
		}else if (e instanceof AuthMethodNotAccessException) {
			mapper.writeValue(response.getWriter(),ResultBody.error(LoginErrorInfoEnum.ERROR_AUTH_FORBIDDEN));
		}else {
			mapper.writeValue(response.getWriter(), ResultBody.error(LoginErrorInfoEnum.ERROR_TOKEN_INVALID, e.getMessage()));
		}
	}
}
