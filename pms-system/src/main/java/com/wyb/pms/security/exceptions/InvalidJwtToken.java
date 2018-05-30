package com.wyb.pms.security.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * JwtTokenNotValid
 *
 * @author alean wang
 */
public class InvalidJwtToken extends AuthenticationException {
    private static final long serialVersionUID = -294671188037098603L;

    public InvalidJwtToken(String msg) {
        super(msg);
    }

    public InvalidJwtToken() {
        super("");
    }
}
