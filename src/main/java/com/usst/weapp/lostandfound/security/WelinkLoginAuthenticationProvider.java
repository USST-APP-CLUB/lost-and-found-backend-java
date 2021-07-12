package com.usst.weapp.lostandfound.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usst.weapp.lostandfound.constants.Constant;
import com.usst.weapp.lostandfound.model.entity.UserDO;
import com.usst.weapp.lostandfound.service.UserService;
import com.usst.weapp.lostandfound.utils.HttpClientUtil;
import com.usst.weapp.lostandfound.utils.JSONUtil;
import com.usst.weapp.lostandfound.utils.MongoUtil;
import com.usst.weapp.lostandfound.utils.WelinkAccessTokenUtil;
import lombok.SneakyThrows;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.json.Json;

import java.io.IOException;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Future;

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

    @Autowired
    private UserService userService;

    @Autowired
    private MongoTemplate mongoTemplate;

    private Map<String, String> headers;

    private Map<String, String> body;


    @Override @SneakyThrows(IOException.class)
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        this.initMaps();

        // 验证用户 code 是每个用户不同的。
        String code = (String) authentication.getCredentials();
        // accesstoken通用。
        String accessToken = welinkAccessTokenUtil.getAccessToken();

        // 这里的id是welink统一id。
        String userId = this.getUserId(code, accessToken);

        // 查数据库
        Map<String, Object> queryConditions = new HashMap<>();
        queryConditions.put("userWelinkId", userId);

//        Query query = new Query(Criteria.where("userId").is("1811410626"));
//        System.out.println(mongoTemplate.findOne(query, UserDO.class, "user"));

//        List<UserDO> users = userService.listByMap(queryConditions);
        List<UserDO> users = userService.findByMap(
                new String[]{"userWelinkId"}, new String[]{userId}, UserDO.class, "user");
        UserDO user = null;
        // 判断查询结果
        if (users.size() == 0){
            // 如果没查到，就从welink获取, 并插入到mongodb
            String userInfoStr = this.getUserInfoStr(userId, accessToken);
            user = this.getUser(userInfoStr); // welink获取的
            user = (UserDO) userService.save(user, "user"); // 向mongodb插入后返回的
        } else {
            // 如果查到了，就直接用查到的，并且异步检查更新。
            user = users.get(0);
            try {
                this.refreshUser(user, userId, accessToken);
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

    @Async
    void refreshUser(UserDO userFromDB, String userId, String accessToken) throws IOException, ParseException {
        // 从welink查用户
        String welinkUserInfoStr = this.getUserInfoStr(userId, accessToken);
//        System.out.println(welinkUserInfoStr); // success
        UserDO userFromWelink = this.getUser(welinkUserInfoStr);
//        System.out.println(userFromWelink);
//        System.out.println("userwelink" + userFromWelink);

        // 解析时间
//        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String welinkTime = userFromWelink.getWelinkLastUpdatedTime();
        String dbTime = userFromDB.getWelinkLastUpdatedTime();
        System.out.println(welinkTime + dbTime);;
        LocalDate welinkLastUpdate = LocalDate.parse(welinkTime);
        LocalDate dbLastUpdate = LocalDate.parse(dbTime);


        // 如果更新时间相等，并且权限相等，则无需更新。
        if (welinkLastUpdate.equals(dbLastUpdate)
                && userFromWelink.getAuthorityStr().equals(userFromDB.getAuthorityStr()))
            return;
        // 如果有一个不相等，则需要更新
        Map<String, Object> updateConditions = new HashMap<>();
        updateConditions.put("departments", userFromWelink.getDepartments());
        updateConditions.put("welinkLastUpdatedTime", userFromWelink.getWelinkLastUpdatedTime());
        updateConditions.put("authorityStr", userFromWelink.getAuthorityStr());
        boolean modifiedCount = userService.updateByWelinkId(
                new String[]{"departments", "welinkLastUpdatedTime", "authorityStr"},
                new String[]{String.valueOf(userFromWelink.getDepartments()), userFromWelink.getWelinkLastUpdatedTime(), userFromWelink.getAuthorityStr()},
                userFromWelink.getUserWelinkId(), UserDO.class ,"user"
        );
//        long modifiedCount = userService.updateByMongoDBId( userFromDB.getMongoDBId(), updateConditions );
        System.out.println(modifiedCount);
    }

    /**
     *
     * @param userInfoStr welink接口里查出来的用户信息串
     * @return
     * @throws IOException
     */
    private UserDO getUser(String userInfoStr) throws IOException {
        // 解析json串
        JsonNode jsonNode = new ObjectMapper().readTree(userInfoStr);
        String userId = JSONUtil.getStringValue(jsonNode, "corpUserId");
        String userWelinkId = JSONUtil.getStringValue(jsonNode, "userId");
        // 自动生成昵称 和 和随机头像
        String nickname = "用户" + new Random().nextInt(100000);
        String randomAvatarUrl = Constant.RANDOM_AVATER_API_URL + nickname + Constant.RANDOM_AVATER_FORMAT_SVG;
        String welinkLastUpdatedTime = JSONUtil.getStringValue(jsonNode, "lastUpdatedTime");
        String userMainDeptCode = JSONUtil.getStringValue(jsonNode, "mainDeptCode");
//        String[] userDepts = this.getUserDepts(userMainDeptCode);
        List<String> userDepts = this.getUserDepts(userMainDeptCode);
        // 获取用户权限
        String authorityStr = this.getAuthorityStr(userWelinkId, welinkAccessTokenUtil.getAccessToken());
        // 回传用户
//        System.out.println(userId + userWelinkId + nickname + randomAvatarUrl);
//        return new UserDO(userId, userWelinkId, nickname, randomAvatarUrl, userDepts, welinkLastUpdatedTime, authorityStr);
        return new UserDO(userId, userWelinkId, nickname, randomAvatarUrl, userDepts, welinkLastUpdatedTime, authorityStr);
    }

    /**
     *
     * @param userMainDeptCode welink侧的部门编码（一个用户只有一个主要部门）
     * @return
     * @throws IOException
     */
//    private String[] getUserDepts(String userMainDeptCode) throws IOException {
    private List<String> getUserDepts(String userMainDeptCode) throws IOException {
        // 请求父部门
        this.resetMaps();
        headers.put("Accept-Charset", "UTF-8");
        headers.put("Content-Type", "application/json");
        headers.put("x-wlk-Authorization", welinkAccessTokenUtil.getAccessToken());
        headers.put("lang", "zh");
        body.put("type", "0");
        body.put("deptCode", userMainDeptCode);
        String result = httpClientUtil.postJsonToWelink(Constant.WELINK_GET_PARENT_DEPARTMENTS, body, headers);

        // 获取父部门数组
        JsonNode jsonNode = new ObjectMapper().readTree(result).get("data");
//        return getUserDeptArr(jsonNode);
        return getUserDeptList(jsonNode);
    }




    /**
     *
     * @param jsonNode node 结构
     *      "data": [{
     *             "deptAllCode": "1003>>2003>>3003",                           //所有上级部门Id, 必填
     *             "deptAllNameCn": "一级部门>>二级部门>>测试本部门",             //所有上级部门中文名称
     *             "deptAllNameEn": "1stDeptName>>2ndDeptName>>TestDeptName"  //所有上级部门英文名称
     *          },
     *          {
     *              "deptAllCode": "6003>>7003>>8003",                           //所有上级部门Id, 必填
     *             "deptAllNameCn": "AA部门>>BB部门>>CC测试本部门",             //所有上级部门中文名称
     *             "deptAllNameEn": "AADeptName>>BBDeptName>>CCTestDeptName"  //所有上级部门英文名称
     *          }
     *      ]
     * @return
     * @throws JsonProcessingException
     */
    private String[] getUserDeptArr(JsonNode jsonNode) throws JsonProcessingException {
        assert jsonNode.isArray();
        JsonNode subJsonNode = jsonNode.get(0);
        String deptCode = JSONUtil.getStringValue(subJsonNode, "deptAllCode");
//        System.out.println(deptCode);
        return deptCode.split(">>");
    }

    private List<String> getUserDeptList(JsonNode jsonNode) throws JsonProcessingException {
        assert jsonNode.isArray();
        JsonNode subJsonNode = jsonNode.get(0);
        String deptCode = JSONUtil.getStringValue(subJsonNode, "deptAllCode");

        return Arrays.asList(deptCode.split(">>"));

    }

    private List<GrantedAuthority> getUserAuthority(String authority) throws IOException {
//        String authority = this.getAuthorityStr(userId, accessToken);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }

    /**
     *
     * @param userId welink侧的用户id
     * @param accessToken 服务端凭证
     * @return
     * @throws IOException
     */
    private String getAuthorityStr(String userId, String accessToken) throws IOException {
        this.resetMaps();
        headers.put("x-wlk-Authorization", accessToken);
        body.put("userId", userId);
        String roleStr = httpClientUtil.getJsonFromWelink(Constant.WELINK_CHECK_USER_APP_ADMIN, body, headers);
        boolean isAdmin = Boolean.getBoolean(JSONUtil.getStringValue(roleStr, "isAdmin"));
        return isAdmin ? Constant.SYS_ROLE_ADMIN : Constant.SYS_ROLE_USER;
    }


    private String getUserId(String code, String accessToken) throws IOException {
        // 使用accessToken 获取 userId
        this.resetMaps();
        headers.put("x-wlk-Authorization", accessToken);
        body.put("code", code);
        String idString = httpClientUtil.getJsonFromWelink(Constant.WELINK_GET_USERID_URL, body, headers);
        String userId = JSONUtil.getStringValue(idString, "userId");
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
//        System.out.println(httpClientUtil.postJsonToWelink(Constant.WELINK_GET_USERINFO_URL_V2, body, headers));
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
