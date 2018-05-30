package com.wyb.pms.security.endpoint;

import com.wyb.pms.common.result.ResultBody;
import com.wyb.pms.common.utils.MapUtils;
import com.wyb.pms.redis.RedisService;
import com.wyb.pms.security.auth.jwt.extractor.TokenExtractor;
import com.wyb.pms.security.auth.jwt.verifier.TokenVerifier;
import com.wyb.pms.security.config.JwtSettings;
import com.wyb.pms.security.model.UserContext;
import com.wyb.pms.security.model.token.JwtToken;
import com.wyb.pms.security.model.token.JwtTokenFactory;
import com.wyb.pms.service.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * RefreshTokenEndpoint
 *
 * @author alean wang
 */
@RestController
public class CreateTokenEndpoint {
    @Autowired
    private JwtTokenFactory tokenFactory;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private LoginService userService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private JwtSettings jwtSettings;


    @RequestMapping(value = "/api/auth/userlogin", method = RequestMethod.GET)
    public ResultBody login(@RequestParam Map params) {
        String username = (String) params.get("username");
        String password = (String) params.get("password");

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new AuthenticationServiceException("用户名或密码不能为空");
        }

        Map userMap = userService.loadUserByUsername(username);
        if (userMap == null) {
            throw new BadCredentialsException("Authentication Failed. user not exists.");
        }
        BCryptPasswordEncoder pwdEncode = new BCryptPasswordEncoder();
        if (!encoder.matches(password, pwdEncode.encode(MapUtils.getString(userMap, "user_pwd")))) {
            throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
        }


        List<String> roleList = (List<String>) userMap.get("roles");
        List<String> authrityList = (List<String>) userMap.get("authorities");

        if (roleList == null) {
            throw new InsufficientAuthenticationException("User has no roles assigned");
        }
        if (authrityList == null) {
            throw new InsufficientAuthenticationException("User has no authority assigned");
        }

        List<GrantedAuthority> authorities = authrityList.stream()
                .map(authority -> new SimpleGrantedAuthority(authority))
                .collect(Collectors.toList());


        UserContext userContext = UserContext.create(MapUtils.getString(userMap, "user_name"), authorities);

        JwtToken accessToken = tokenFactory.createAccessJwtToken(userContext);
        JwtToken refreshToken = tokenFactory.createRefreshToken(userContext);

        Map<String, String> tokenMap = new HashMap<String, String>();
        tokenMap.put("token", accessToken.getToken());
        tokenMap.put("refreshToken", refreshToken.getToken());

        Map redisUserMap = MapUtils.exclude(userMap, "roles", "authorities");

        String tokenKey = accessToken.getToken().toString();
        redisService.hmSetAll(tokenKey+":userinfo", redisUserMap, Long.valueOf(jwtSettings.getTokenExpirationTime()*60));
        for (String authrity : authrityList) {
            redisService.add(tokenKey + ":authrity", authrity, Long.valueOf(jwtSettings.getTokenExpirationTime()*60));
        }
        return ResultBody.sucess(tokenMap);
    }
}
