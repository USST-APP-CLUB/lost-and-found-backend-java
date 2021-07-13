package com.usst.weapp.lostandfound.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usst.weapp.lostandfound.constants.enums.ResultCodeEnum;
import com.usst.weapp.lostandfound.model.common.Response;
import com.usst.weapp.lostandfound.model.entity.UserDO;
import com.usst.weapp.lostandfound.service.UserService;
import com.usst.weapp.lostandfound.utils.JSONUtil;
import com.usst.weapp.lostandfound.utils.JWTUtils;
import com.usst.weapp.lostandfound.utils.MyException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Sunforge
 * @Date 2021-07-12 10:03
 * @Version V1.0.0
 * @Description
 */
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private UserService userService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwt =  request.getHeader(jwtUtils.getHeader());
        if(!StringUtils.hasText(jwt)){
            chain.doFilter(request, response);
            return;
        }
        Claims claim = jwtUtils.getClaimByToken(jwt);
        if(claim == null){
            response.setContentType("application/json;charset=UTF-8");
            ServletOutputStream outputStream = response.getOutputStream();

            Response res = Response.fail(ResultCodeEnum.JWT_ERROR);
            ObjectMapper mapper = new ObjectMapper();
            outputStream.write(mapper.writeValueAsString(res).getBytes("UTF-8"));

            outputStream.flush();
            outputStream.close();
        }

        if(jwtUtils.isTokenExpired(claim)){
            response.setContentType("application/json;charset=UTF-8");
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ServletOutputStream outputStream = response.getOutputStream();

            Response res = Response.fail(ResultCodeEnum.JWT_EXPIRE);
            ObjectMapper mapper = new ObjectMapper();
            outputStream.write(mapper.writeValueAsString(res).getBytes("UTF-8"));

            outputStream.flush();
            outputStream.close();
        }
        String userWelinkId = claim.getSubject();  // username 就是userId
        System.out.println("jwt filter "+userWelinkId);

        Map<String, Object> queryConditions = new HashMap<>();
        queryConditions.put("userWelinkId", userWelinkId);
        List<UserDO> users = userService.find(queryConditions, UserDO.class, "user");
        if (users.size() != 1) throw new MyException(ResultCodeEnum.JWT_ERROR,"JWT异常导致无法从数据库中加载用户");
        UserDO user = users.get(0);

        // 获取用户权限
        Collection<? extends GrantedAuthority> authorities = this.getUserAuthority(user.getAuthorityStr());

//        WelinkLoginAuthenticationToken token = new WelinkLoginAuthenticationToken(JSONUtil.objToStr(user), null, authorities);
        WelinkLoginAuthenticationToken token = new WelinkLoginAuthenticationToken(user.getUserWelinkId(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(token);
        chain.doFilter(request, response);
    }

    private List<GrantedAuthority> getUserAuthority(String authority) throws IOException {
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }
}
