package com.usst.weapp.lostandfound.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author Sunforge
 * @Date 2021-06-02 19:15
 */
@Data
@Component
@ConfigurationProperties(prefix = "lostandfound.jwt")
public class JWTUtils {
    // 此处属性是通过配置文件注入的，需要到配置文件去改。
    private long expire;
    private String secret;
    private String header;

    // 生成jwt
    public String generateToken(String userId) {
        Date nowDate = new Date();
        Date expireDate  = new Date(nowDate.getTime() + 1000 * expire);

        return Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setSubject(userId)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    // 解析jwt
    public Claims getClaimByToken(String jwt){
        try{ // 如果jwt被篡改，就不报错。
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception e){
            return null;
        }
    }

    // jwt是否过期
    public Boolean isTokenExpired(Claims claims){
        return claims.getExpiration().before(new Date());
    }

}
