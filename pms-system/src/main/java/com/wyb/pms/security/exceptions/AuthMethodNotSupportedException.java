package com.wyb.pms.security.exceptions;

import org.springframework.security.authentication.AuthenticationServiceException;

/**
 *
 * @author alean wang
 */
public class AuthMethodNotSupportedException extends AuthenticationServiceException {
    private static final long serialVersionUID = 3705043083010304496L;

    public AuthMethodNotSupportedException(String msg) {
        super(msg);
    }
}
