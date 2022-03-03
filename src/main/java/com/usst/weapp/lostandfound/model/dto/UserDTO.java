package com.usst.weapp.lostandfound.model.dto;

import com.usst.weapp.lostandfound.constants.Constant;
import com.usst.weapp.lostandfound.model.entity.UserDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

/**
 * @Author Sunforge
 * @Date 2021-07-13 10:44
 * @Version V1.0.0
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String nickname;
    private String avatarUrl;
//    private String userAuthority;
    private Boolean isAdmin;

    public UserDTO (UserDO user){
        this.nickname = user.getNickname();
        this.avatarUrl = user.getRandomAvatarUrl();
//        this.userAuthority = user.getAuthorityStr();
        this.isAdmin = user.getAuthorityStr().equals(Constant.SYS_ROLE_ADMIN);
    }
}
