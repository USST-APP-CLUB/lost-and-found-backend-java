package com.usst.weapp.lostandfound.constants;

/**
 * @Author Sunforge
 * @Date 2021-07-10 10:57
 * @Version V1.0.0
 * @Description
 */
public final class Constant {
    private Constant(){}
    public static final String WELINK_CLIENT_ID = "20210705093731544077254";
    public static final String WELINK_CLIENT_SECRET = "9f24b5bd-6451-48fc-9403-86e36bd8f34c";
    public static final String WELINK_RESPONSE_SUCCESS = "0";
    public static final String WELINK_GET_ACCESS_TOKEN_URL = "https://open.welink.huaweicloud.com/api/auth/v2/tickets";
    public static final String WELINK_GET_USERID_URL = "https://open.welink.huaweicloud.com/api/auth/v2/userid";
    public static final String WELINK_GET_USERINFO_URL_V2 = "https://open.welink.huaweicloud.com/api/contact/v2/user/detail";
    public static final String WELINK_CHECK_USER_APP_ADMIN = "https://open.welink.huaweicloud.com/api/weopen/v1/isadmin";
    public static final String WELINK_RESPONSE_USER_NOT_ADMIN = "58003";
    public static final String SYS_ROLE_USER = "ROLE_user";
    public static final String SYS_ROLE_ADMIN = "ROLE_admin";
}
