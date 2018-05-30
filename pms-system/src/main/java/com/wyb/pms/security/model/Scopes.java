package com.wyb.pms.security.model;

/**
 * Scopes
 *
 * @author alean wang
 */
public enum Scopes {
    REFRESH_TOKEN;
    
    public String authority() {
        return "ROLE_" + this.name();
    }
}
