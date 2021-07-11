package com.usst.weapp.lostandfound.aspect;

import com.usst.weapp.lostandfound.constants.Constant;
import com.usst.weapp.lostandfound.utils.JSONUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author Sunforge
 * @Date 2021-07-10 20:35
 * @Version V1.0.0
 * @Description
 */
@Aspect
@Component
public class WelinkApiAspect {


    @Pointcut("execution(* com.usst.weapp.lostandfound.utils.HttpClientUtil.*Welink(..))")
    public void checkResult(){

    }

    @AfterReturning(value = "checkResult()", returning = "returnValue")     // 返回通知
    public void checkCode(JoinPoint jp, Object returnValue) throws IOException {
        String resultStr = returnValue.toString();
        String code = JSONUtil.getStringJsonValue(resultStr, "code");
        if(!code.equals(Constant.WELINK_RESPONSE_SUCCESS)){
            // 1000 token失效或不可用
            System.out.println(code);
            String message = JSONUtil.getStringJsonValue(resultStr, "message");
            System.out.println(message);

            if(!code.equals(Constant.WELINK_RESPONSE_USER_NOT_ADMIN)){
                throw new AuthenticationServiceException(message);
            }
        }
    }
}
