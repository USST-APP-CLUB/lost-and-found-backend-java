package com.usst.weapp.lostandfound.model.mongomap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Sunforge
 * @Date 2021-07-13 20:54
 * @Version V1.0.0
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WelinkAccessToken {
//    private String key;
    private String accessToken;
    private String expireTimeStr;
}
