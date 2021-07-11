package com.usst.weapp.lostandfound.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usst.weapp.lostandfound.model.common.Response;
import com.usst.weapp.lostandfound.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
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
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    JWTUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 返回JWT token
        response.setContentType("application/json;charset=UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        // welinkloginAuthenticationProvider里面new的AuthenticationToken里面principal是啥，这里getname就是啥
        System.out.println(authentication.getName());
        String jwt = jwtUtils.generateToken(authentication.getName());
        response.setHeader(jwtUtils.getHeader(), jwt);

        Response result = Response.success("登录成功");
        String responseJson = mapper.writeValueAsString(result);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(responseJson.getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }
}
