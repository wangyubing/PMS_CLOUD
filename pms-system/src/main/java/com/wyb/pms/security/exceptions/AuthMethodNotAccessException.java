package com.wyb.pms.security.exceptions;

import com.wyb.pms.security.model.token.JwtToken;
import org.springframework.security.core.AuthenticationException;

/**
 *
 * @author alean wang
 */
public class AuthMethodNotAccessException extends AuthenticationException {
    private static final long serialVersionUID = -5959543783324224864L;

    public AuthMethodNotAccessException(String msg) {
        super(msg);
    }

}
