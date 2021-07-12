package com.usst.weapp.lostandfound.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * @Author Sunforge
 * @Date 2021-07-11 14:09
 * @Version V1.0.0
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user")
public class UserDO {
    private static final long serialVersionUID = -3952031579230856613L;
    @Id
    private String Id;
    private String userId;
    private String userWelinkId;
    private String nickname;
    private String randomAvatarUrl;
    private List<String> departments;
    private String welinkLastUpdatedTime;
    private String authorityStr;
//    private String _class;

    public UserDO(String userId, String userWelinkId, String nickname, String randomAvatarUrl, List<String> departments, String welinkLastUpdatedTime, String authorityStr) {
        this.userId = userId;
        this.userWelinkId = userWelinkId;
        this.nickname = nickname;
        this.randomAvatarUrl = randomAvatarUrl;
        this.departments = departments;
        this.welinkLastUpdatedTime = welinkLastUpdatedTime;
        this.authorityStr = authorityStr;
    }



}
