package com.usst.weapp.lostandfound.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usst.weapp.lostandfound.constants.enums.ResultCodeEnum;
import com.usst.weapp.lostandfound.model.common.Response;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author Sunforge
 * @Date 2021-07-13 9:32
 * @Version V1.0.0
 * @Description
 */
@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ServletOutputStream outputStream = response.getOutputStream();

        Response res = Response.fail(ResultCodeEnum.RQ_UNAUTHORIZED);
        ObjectMapper mapper = new ObjectMapper();
        outputStream.write(mapper.writeValueAsString(res).getBytes("UTF-8"));

        outputStream.flush();
        outputStream.close();
    }
}
