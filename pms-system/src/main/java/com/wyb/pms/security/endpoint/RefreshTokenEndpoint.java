package com.wyb.pms.security.endpoint;

import com.wyb.pms.common.utils.MapUtils;
import com.wyb.pms.config.security.WebSecurityConfig;
import com.wyb.pms.security.UserService;
import com.wyb.pms.security.auth.jwt.extractor.TokenExtractor;
import com.wyb.pms.security.auth.jwt.verifier.TokenVerifier;
import com.wyb.pms.security.config.JwtSettings;
import com.wyb.pms.security.exceptions.InvalidJwtToken;
import com.wyb.pms.security.model.UserContext;
import com.wyb.pms.security.model.token.JwtToken;
import com.wyb.pms.security.model.token.JwtTokenFactory;
import com.wyb.pms.security.model.token.RawAccessJwtToken;
import com.wyb.pms.security.model.token.RefreshToken;
import com.wyb.pms.service.LoginService;
import com.wyb.pms.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * RefreshTokenEndpoint
 *
 * @author alean wang
 */
@RestController
public class RefreshTokenEndpoint {
    @Autowired
    private JwtTokenFactory tokenFactory;
    @Autowired
    private JwtSettings jwtSettings;
    @Autowired
    private LoginService userService;
    @Autowired
    private TokenVerifier tokenVerifier;
    //@Autowired
    //@Qualifier("jwtHeaderTokenExtractor")
    @Resource(name = "jwtHeaderTokenExtractor")
    private TokenExtractor tokenExtractor;
    
    @RequestMapping(value="/api/auth/token", method= RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody
    JwtToken refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tokenPayload = tokenExtractor.extract(request.getHeader(WebSecurityConfig.AUTHENTICATION_HEADER_NAME));
        
        RawAccessJwtToken rawToken = new RawAccessJwtToken(tokenPayload);
        RefreshToken refreshToken = RefreshToken.create(rawToken, jwtSettings.getTokenSigningKey()).orElseThrow(() -> new InvalidJwtToken());

        String jti = refreshToken.getJti();
        if (!tokenVerifier.verify(jti)) {
            throw new InvalidJwtToken();
        }

        String subject = refreshToken.getSubject();
        Map userMap = (Map) userService.loadUserByUsername(subject);
        if (userMap == null) {
            throw new InsufficientAuthenticationException("用户不存在或未找到");
        }
        /*List<String> roleList = (List<String>) userMap.get("roles");
        List<String> authorityList = (List<String>) userMap.get("authorities");
        if (authorityList != null) {
            List<GrantedAuthority> authorities = authorityList.stream()
                    .map(authority -> new SimpleGrantedAuthority(authority))
                    .collect(Collectors.toList());
        }*/

        UserContext userContext = UserContext.create(MapUtils.getString(userMap, "user_name"), null);

        return tokenFactory.createAccessJwtToken(userContext);
    }
}
