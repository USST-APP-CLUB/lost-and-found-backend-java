package com.usst.weapp.lostandfound.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usst.weapp.lostandfound.constants.enums.ResultCodeEnum;
import com.usst.weapp.lostandfound.model.common.Response;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author Sunforge
 * @Date 2021-07-13 9:35
 * @Version V1.0.0
 * @Description
 */
@Component
public class JWTAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        ServletOutputStream outputStream = response.getOutputStream();

        Response responseDTO = Response.fail(ResultCodeEnum.RQ_FORBIDDEN);
        ObjectMapper mapper = new ObjectMapper();
        outputStream.write(mapper.writeValueAsString(responseDTO).getBytes("UTF-8"));

        outputStream.flush();
        outputStream.close();
    }
}
