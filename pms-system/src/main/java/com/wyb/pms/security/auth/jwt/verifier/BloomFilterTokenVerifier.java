package com.wyb.pms.security.auth.jwt.verifier;

import org.springframework.stereotype.Component;

/**
 * BloomFilterTokenVerifier
 *
 * @author alean wang
 */
@Component
public class BloomFilterTokenVerifier implements TokenVerifier {
    @Override
    public boolean verify(String jti) {
        return true;
    }
}
