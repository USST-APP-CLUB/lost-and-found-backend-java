package com.usst.weapp.lostandfound.security;

import com.usst.weapp.lostandfound.utils.JSONUtil;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * @Author Sunforge
 * @Date 2021-07-10 10:20
 */
public class WelinkLoginAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private boolean postOnly = true;

    public WelinkLoginAuthenticationProcessingFilter() {
        super(new AntPathRequestMatcher("/welink/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        // 提取前端传来的免登授权码code
        String requestJson = ReadRequestBody(request);
        String code = JSONUtil.getStringValue(requestJson, "code");
        WelinkLoginAuthenticationToken authRequest =  new WelinkLoginAuthenticationToken(null, code);
        return this.getAuthenticationManager().authenticate(authRequest);
    }



    public static String ReadRequestBody(HttpServletRequest request) throws IOException {
//        InputStreamReader isr = new InputStreamReader((ServletInputStream) request.getInputStream(), "utf-8");
        BufferedReader br = new BufferedReader(request.getReader());
        StringBuilder sb = new StringBuilder("");
        String temp;
        while ((temp = br.readLine()) != null) {
            sb.append(temp);
        }
        br.close();
        return sb.toString();
    }

}
