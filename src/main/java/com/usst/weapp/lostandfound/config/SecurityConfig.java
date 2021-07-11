package com.usst.weapp.lostandfound.config;

import com.usst.weapp.lostandfound.security.LoginFailureHandler;
import com.usst.weapp.lostandfound.security.LoginSuccessHandler;
import com.usst.weapp.lostandfound.security.WelinkLoginAuthenticationProcessingFilter;
import com.usst.weapp.lostandfound.security.WelinkLoginAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

/**
 * @Author Sunforge
 * @Date 2021-07-10 9:54
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private WelinkLoginAuthenticationProvider welinkLoginAuthenticationProvider;

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LoginFailureHandler loginFailureHandler;


//    @Autowired
//    private WelinkLoginAuthenticationProcessingFilter welinkLoginFilter;


//    @Autowired
//    private ValidateCodeFilter validateCodeFilter;


//    @Autowired
//    LoginFailureHandler loginFailureHandler;
//
//    @Autowired
//    LoginSuccessHandler loginSuccessHandler;
//

//    @Autowired
//    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//
//    @Autowired
//    JwtAccessDeniedHandler jwtAccessDeniedHandler;
//
//    @Autowired
//    UserDetailServiceImpl userDetailService;
//
//    @Autowired
//    JwtLogoutSuccessHandler jwtLogoutSuccessHandler;
//
//    @Bean
//    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
//        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
//        return jwtAuthenticationFilter;
//    }

//    @Bean
//    BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
    private static final String[] URL_WHITELIST = {

//            "/",
            "/login",
            "/welink/login",
            "/logout",
            "/test/**",

            // swagger ui
            "/v2/api-docs",
            "/swagger-resources/configuration/ui",
            "/swagger-resources",
            "/swagger-resources/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",


    };


    protected void configure(HttpSecurity http) throws Exception {

        http.cors().and().csrf().disable();

        // 第三方登录配置
        http.addFilterBefore(welinkLoginFilter(), AbstractPreAuthenticatedProcessingFilter.class);

        // 取消默认的表单登录。
//        http.formLogin()
//            .loginProcessingUrl("/login");
//            .successHandler(loginSuccessHandler)
//            .failureHandler(loginFailureHandler)

//            .and()
//            .logout()
//                .logoutSuccessHandler(jwtLogoutSuccessHandler)

            // 禁用session
        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            // 配置拦截规则
            .and()
            .authorizeRequests()
            .antMatchers(URL_WHITELIST).permitAll()
            .anyRequest().authenticated()

            // 异常处理器
//            .and()
//            .exceptionHandling()
//                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//                .accessDeniedHandler(jwtAccessDeniedHandler)

            // 配置自定义的过滤器
//                .and()
//                .addFilter(jwtAuthenticationFilter())
//                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class)

        ;

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailService);
        // 注册自定义认证器
        auth.authenticationProvider(welinkLoginAuthenticationProvider);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public WelinkLoginAuthenticationProcessingFilter welinkLoginFilter(){
        WelinkLoginAuthenticationProcessingFilter filter = new WelinkLoginAuthenticationProcessingFilter();
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationSuccessHandler(loginSuccessHandler);
        filter.setAuthenticationFailureHandler(loginFailureHandler);
        filter.setFilterProcessesUrl("/welink/login");
        return filter;
    }

}
