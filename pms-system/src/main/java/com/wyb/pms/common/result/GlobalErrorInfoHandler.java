package com.wyb.pms.common.result;

import com.wyb.pms.common.result.constant.LoginErrorInfoEnum;
import com.wyb.pms.security.exceptions.AuthMethodNotAccessException;
import com.wyb.pms.security.exceptions.AuthMethodNotSupportedException;
import com.wyb.pms.security.exceptions.InvalidJwtToken;
import com.wyb.pms.security.exceptions.JwtExpiredTokenException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 统一错误码异常处理
 *
 * Created by bysocket on 14/03/2017.
 */
@RestControllerAdvice
public class GlobalErrorInfoHandler {

    @ExceptionHandler(value = GlobalErrorInfoException.class)
    public ResultBody errorHandlerOverJson(HttpServletRequest request,
                                           GlobalErrorInfoException exception) {
        ErrorInfoInterface errorInfo = exception.getErrorInfo();
        ResultBody result = new ResultBody(errorInfo);
        return result;
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public ResultBody errorHandlerOverJson(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        if (exception instanceof InvalidJwtToken) {
            return new ResultBody(LoginErrorInfoEnum.ERROR_TOKEN_INVALID);
        }else if (exception instanceof InsufficientAuthenticationException) {
            return new ResultBody(LoginErrorInfoEnum.ERROR_USER_UNFIND);
        }else if (exception instanceof AuthMethodNotSupportedException) {
            return new ResultBody(LoginErrorInfoEnum.ERROR_AUTH_FORBIDDEN);
        }else if (exception instanceof AuthMethodNotAccessException) {
            return new ResultBody(LoginErrorInfoEnum.ERROR_AUTH_FORBIDDEN);
        }else if (exception instanceof JwtExpiredTokenException) {
            return new ResultBody(LoginErrorInfoEnum.ERROR_TOKEN_EXPIRE);
        }else if (exception instanceof BadCredentialsException){
            return new ResultBody(LoginErrorInfoEnum.ERROR_AUTH_UNAUTHORIZED);
        } else {
            return new ResultBody(LoginErrorInfoEnum.ERROR_TOKEN_INVALID, exception.getMessage());
        }
    }
}
