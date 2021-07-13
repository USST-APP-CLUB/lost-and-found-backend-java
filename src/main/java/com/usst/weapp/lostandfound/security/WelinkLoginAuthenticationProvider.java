package com.usst.weapp.lostandfound.security;


import com.usst.weapp.lostandfound.model.entity.UserDO;
import com.usst.weapp.lostandfound.service.UserService;
import com.usst.weapp.lostandfound.utils.AsyncUtil;
import com.usst.weapp.lostandfound.utils.HttpClientUtil;
import com.usst.weapp.lostandfound.utils.WelinkUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

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
    private WelinkUtil welinkUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private AsyncUtil asyncUtil;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override @SneakyThrows
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // 验证用户 code 是每个用户不同的。
        String code = (String) authentication.getCredentials();

        // 这里的id是welink统一id。
        String userId = welinkUtil.getUserId(code);

        // 查数据库
        Map<String, Object> queryConditions = new HashMap<>();
        queryConditions.put("userWelinkId", userId);
        List<UserDO> users = userService.find(queryConditions, UserDO.class, "user");
        UserDO user = null;
        // 判断查询结果
        if (users.size() == 0){
            // 如果没查到，就从welink获取, 并插入到mongodb
            user = welinkUtil.getUserByUserId(userId);
            user = (UserDO) userService.save(user, "user"); // 向mongodb插入后返回的
        } else {
            // 如果查到了，就直接用查到的，并且异步检查更新。
            user = users.get(0);
            try {
                asyncUtil.refreshUser(user, userId);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // 获取用户权限
        Collection<? extends GrantedAuthority> authorities = this.getUserAuthority(user.getAuthorityStr());
        return new WelinkLoginAuthenticationToken(user, null, authorities);
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

    private List<GrantedAuthority> getUserAuthority(String authority) throws IOException {
//        String authority = this.getAuthorityStr(userId, accessToken);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }

}
