package com.wyb.pms.security.auth.jwt.verifier;

/**
 *
 * @author alean wang
 */
public interface TokenVerifier {
    public boolean verify(String jti);
}
