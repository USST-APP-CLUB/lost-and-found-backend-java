package com.usst.weapp.lostandfound.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usst.weapp.lostandfound.constants.Constant;
import com.usst.weapp.lostandfound.model.entity.UserDO;
import com.usst.weapp.lostandfound.model.mongomap.MongoMap;
import com.usst.weapp.lostandfound.model.mongomap.WelinkAccessToken;
import com.usst.weapp.lostandfound.service.KeyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Author Sunforge
 * @Date 2021-07-11 19:10
 * @Version V1.0.0
 * @Description
 */

@Component
public class WelinkUtil {

    @Autowired
    private HttpClientUtil httpClientUtil;

    @Autowired
    private KeyValueService keyValueService;

    @Autowired
    private TimeUtil timeUtil;

    public String getAccessToken() throws IOException {
        String accessToken = getAccessTokenFromDB();
        if (accessToken != null) return accessToken;
        return getAccessTokenFromWelink();
    }

    // 从数据库获取
    private String getAccessTokenFromDB(){
        Map<String, Object> queryConditions = new HashMap<>();
        queryConditions.put("key", "access_token");
        List<MongoMap> list = keyValueService.find(queryConditions, MongoMap.class, "key_value");
//        System.out.println(list);
        if (list == null || list.size() == 0) return null;
//        System.out.println(list == null);
        WelinkAccessToken accessToken = (WelinkAccessToken) list.get(0).getValue();
//        assert accessToken != null;
        System.out.println("db 命中" + accessToken.getAccessToken());
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime expireTime = timeUtil.convertStringToTime(accessToken.getExpireTimeStr());
        if (nowTime.isAfter(expireTime)) return null;
        return accessToken.getAccessToken();
    }

    // 从welink获取
    private String getAccessTokenFromWelink() throws IOException {
        Map<String ,String> body = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        body.put("client_id", Constant.WELINK_CLIENT_ID);
        body.put("client_secret", Constant.WELINK_CLIENT_SECRET);
        String resultStr = httpClientUtil.postJsonToWelink(Constant.WELINK_GET_ACCESS_TOKEN_URL, body, headers);
        // 提取access_token 和有效时长
        String access_token = JSONUtil.getStringValue(resultStr, "access_token");
        Integer expire_in = JSONUtil.getIntNumberValue(resultStr, "expires_in");
        // 转化时间str
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(expire_in - 300);
        String format = "yyyy-MM-dd HH:mm:ss";
        String expireTimeStr = expireTime.format(DateTimeFormatter.ofPattern(format));
        WelinkAccessToken welinkAccessToken = new WelinkAccessToken(access_token, expireTimeStr);
        MongoMap mongoMap = new MongoMap("access_token", welinkAccessToken);
        Map<String, Object> queryConditions = new HashMap<>();
        Map<String, Object> updateConditions = new HashMap<>();
        queryConditions.put("key", "access_token");
        updateConditions.put("value", mongoMap);
        keyValueService.update(updateConditions, queryConditions, "key_value");
//        keyValueService.save(welinkAccessToken, "key_value");
        return access_token;
    }

    public UserDO getUserByCode(String code) throws IOException {
        String userId = this.getUserId(code);
        String userInfoStr = this.getUserInfoStr(userId);
        return this.convertUserStrToUser(userInfoStr);
    }

    public UserDO getUserByUserId(String userId) throws IOException {
        String userInfoStr = this.getUserInfoStr(userId);
        return this.convertUserStrToUser(userInfoStr);
    }

    public UserDO getUserByUserInfoStr(String userInfoStr) throws IOException {
        return this.convertUserStrToUser(userInfoStr);
    }

    public String getUserId(String code) throws IOException {
        // 用code和access_token获取userId
        Map<String, String> headers = new HashMap<>();
        Map<String, String> body = new HashMap<>();
        headers.put("x-wlk-Authorization", this.getAccessToken());
        body.put("code", code);
        String idString = httpClientUtil.getJsonFromWelink(Constant.WELINK_GET_USERID_URL, body, headers);
        String userId = JSONUtil.getStringValue(idString, "userId");
        if (userId == null){
            throw new BadCredentialsException("access_token 无效");
        }
        return userId;
    }

    private String getUserInfoStr(String userId) throws IOException {
        // 用userid和access_token获取userInfo(string)
        Map<String, String> headers = new HashMap<>();
        Map<String, String> body = new HashMap<>();
        headers.put("x-wlk-Authorization", this.getAccessToken());
        headers.put("Content-Type", "application/json");
        body.put("userId", userId);
        // 调用成功，返回信息
//        System.out.println(httpClientUtil.postJsonToWelink(Constant.WELINK_GET_USERINFO_URL_V2, body, headers));
        return httpClientUtil.postJsonToWelink(Constant.WELINK_GET_USERINFO_URL_V2, body, headers);
    }

    /**
     *
     * @param userInfoStr welink接口里查出来的用户信息串
     * @return
     * @throws IOException
     */
    private UserDO convertUserStrToUser(String userInfoStr) throws IOException {
        // 解析json串
        JsonNode jsonNode = new ObjectMapper().readTree(userInfoStr);
        // 获取用户id和welinkid
        String userId = JSONUtil.getStringValue(jsonNode, "corpUserId");
        String userWelinkId = JSONUtil.getStringValue(jsonNode, "userId");
        // 自动生成昵称 和 和随机头像
        String nickname = "用户" + new Random().nextInt(100000);
        String randomAvatarUrl = Constant.RANDOM_AVATER_API_URL
                + URLEncoder.encode(nickname, StandardCharsets.UTF_8.name())
                + Constant.RANDOM_AVATER_FORMAT_SVG;
        // 获取welink最后更新时间
        String welinkLastUpdatedTime = JSONUtil.getStringValue(jsonNode, "lastUpdatedTime");
        // 获取用户部门
        String userMainDeptCode = JSONUtil.getStringValue(jsonNode, "mainDeptCode");
        List<String> userDepts = this.getUserDeptsByMainDeptCode(userMainDeptCode);
        // 获取用户权限
        String authorityStr = this.getAuthorityStr(userWelinkId);
        // 回传用户
        return new UserDO(userId, userWelinkId, nickname, randomAvatarUrl, userDepts, welinkLastUpdatedTime, authorityStr);
    }

    private List<String> getUserDeptsByMainDeptCode(String userMainDeptCode) throws IOException {
        // 请求父部门 通过userInfo(string)中解析出的maindeptcode
        Map<String, String> headers = new HashMap<>();
        Map<String, String> body = new HashMap<>();
        headers.put("Accept-Charset", "UTF-8");
        headers.put("Content-Type", "application/json");
        headers.put("x-wlk-Authorization", this.getAccessToken());
        headers.put("lang", "zh");
        body.put("type", "0");
        body.put("deptCode", userMainDeptCode);
        String result = httpClientUtil.postJsonToWelink(Constant.WELINK_GET_PARENT_DEPARTMENTS, body, headers);

        // 获取父部门数组
        JsonNode jsonNode = new ObjectMapper().readTree(result).get("data");
        assert jsonNode.isArray();
        JsonNode subJsonNode = jsonNode.get(0);
        String deptCode = JSONUtil.getStringValue(subJsonNode, "deptAllCode");
        return Arrays.asList(deptCode.split(">>"));
    }

    private String getAuthorityStr(String userId) throws IOException {
        // 通过userid 和access_token 检测用户对本应用的权限。
        Map<String, String> headers = new HashMap<>();
        Map<String, String> body = new HashMap<>();
        headers.put("x-wlk-Authorization", this.getAccessToken());
        body.put("userId", userId);
        String roleStr = httpClientUtil.getJsonFromWelink(Constant.WELINK_CHECK_USER_APP_ADMIN, body, headers);
        boolean isAdmin = JSONUtil.getBooleanValue(roleStr, "isAdmin");
        return isAdmin ? Constant.SYS_ROLE_ADMIN : Constant.SYS_ROLE_USER;
    }



}
