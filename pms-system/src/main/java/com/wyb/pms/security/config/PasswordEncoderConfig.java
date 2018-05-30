package com.wyb.pms.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * PasswordEncoderConfig
 *
 * @author alean wang
 */
@Configuration
public class PasswordEncoderConfig {
    @Bean
    protected BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
