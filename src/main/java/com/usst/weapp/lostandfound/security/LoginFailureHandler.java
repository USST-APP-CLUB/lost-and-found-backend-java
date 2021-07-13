package com.usst.weapp.lostandfound.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usst.weapp.lostandfound.constants.enums.ResultCodeEnum;
import com.usst.weapp.lostandfound.model.common.Response;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author Sunforge
 * @Date 2021-06-02 17:18
 */
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        System.out.println("认证失败了");
//        System.out.println(e);
//        // 可加错误信息*************************
//        System.out.println(e);
        response.setContentType("application/json;charset=UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        e.printStackTrace();
        System.out.println(e.getMessage());
        Response result = Response.fail("welink免密登陆失败，请稍后重试。如仍无法登录，请联系管理员");
        String responseJson = mapper.writeValueAsString(result);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(responseJson.getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }
}
