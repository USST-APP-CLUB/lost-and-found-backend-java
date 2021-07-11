package com.usst.weapp.lostandfound.security;

import com.usst.weapp.lostandfound.constants.Constant;
import com.usst.weapp.lostandfound.utils.HttpClientUtil;
import com.usst.weapp.lostandfound.utils.JSONUtil;
import com.usst.weapp.lostandfound.utils.WelinkAccessTokenUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Sunforge
 * @Date 2021-07-11 11:16
 * @Version V1.0.0
 * @Description
 */
@Component
public class WelinkLoginAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private HttpClientUtil httpClientUtil;

    @Autowired
    private WelinkAccessTokenUtil welinkAccessTokenUtil;

    private Map<String, String> headers;

    private Map<String, String> body;


    @Override @SneakyThrows(IOException.class)
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        this.initMaps();

        // 验证用户 code 是每个用户不同的。
        String code = (String) authentication.getCredentials();
        // accesstoken通用。
        String accessToken = welinkAccessTokenUtil.getAccessToken();

        String userId = this.getUserId(code, accessToken);

        // 如果数据库里有，那么直接认证，如果数据库里没有，就先获详细信息，再授权，存下来。
        String userInfoStr = this.getUserInfoStr(userId, accessToken);


        // 获取用户权限
        Collection<? extends GrantedAuthority> authorities = this.getUserAuthority(userId, accessToken);


        return new WelinkLoginAuthenticationToken(userInfoStr, null, authorities);
    }

    /**
     * providerManager会遍历所有
     * securityconfig中注册的provider集合
     * 根据此方法返回true或false来决定由哪个provider
     * 去校验请求过来的authentication
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return (WelinkLoginAuthenticationToken.class
                .isAssignableFrom(authentication));
    }


    private List<GrantedAuthority> getUserAuthority(String userId, String accessToken) throws IOException {
        this.resetMaps();
        headers.put("x-wlk-Authorization", accessToken);
        body.put("userId", userId);
        String roleStr = httpClientUtil.getJsonFromWelink(Constant.WELINK_CHECK_USER_APP_ADMIN, body, headers);
        boolean isAdmin = Boolean.getBoolean(JSONUtil.getStringJsonValue(roleStr, "isAdmin"));
        String authority = isAdmin ? Constant.SYS_ROLE_ADMIN : Constant.SYS_ROLE_USER;
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }


    private String getUserId(String code, String accessToken) throws IOException {
        // 使用accessToken 获取 userId
        this.resetMaps();
        headers.put("x-wlk-Authorization", accessToken);
        body.put("code", code);
        String idString = httpClientUtil.getJsonFromWelink(Constant.WELINK_GET_USERID_URL, body, headers);
        String userId = JSONUtil.getStringJsonValue(idString, "userId");
        if (userId == null){
            throw new BadCredentialsException("access_token 无效");
        }
        return userId;
    }

    private String getUserInfoStr(String userId, String accessToken) throws IOException {
        this.resetMaps();
        headers.put("x-wlk-Authorization", accessToken);
        headers.put("Content-Type", "application/json");
        body.put("userId", userId);
        // 调用成功，返回信息
        return httpClientUtil.postJsonToWelink(Constant.WELINK_GET_USERINFO_URL_V2, body, headers);
    }

    private void initMaps(){
        this.body = new HashMap<>();
        this.headers = new HashMap<>();
    }

    private void resetMaps(){
        this.body.clear();
        this.headers.clear();
    }




}
