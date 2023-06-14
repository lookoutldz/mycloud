package org.looko.mycloud.user.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.looko.mycloud.commonstarter.entity.ResponseEntity;
import org.looko.mycloud.user.service.AuthorizationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.looko.mycloud.commonstarter.enumeration.ResponseStatusEnum.AUTH_FAILED;
import static org.looko.mycloud.commonstarter.enumeration.ResponseStatusEnum.FORBIDDEN;


@Configuration
public class SecurityConfiguration {

    private final DataSource dataSource;

    private final AuthorizationService authorizationService;

    public SecurityConfiguration(DataSource dataSource, AuthorizationService authorizationService) {
        this.dataSource = dataSource;
        this.authorizationService = authorizationService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, PersistentTokenRepository tokenRepository) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/validcode/**",
                                "/auth/resetPassword/**", "/auth/register", "/user/test/port")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/auth/login")
                        .successHandler(this::onAuthenticationSuccess)
                        .failureHandler(this::onAuthenticationFailure)
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessHandler(this::onAuthenticationSuccess)
                )
                .rememberMe(remember -> remember
                        .rememberMeParameter("remember")
                        .tokenRepository(tokenRepository)
                        .tokenValiditySeconds(3600)
                )
                .exceptionHandling()
                .authenticationEntryPoint(this::commence)
                .and()
                // 如果不关闭 csrf，请求又中没有 csrf 令牌则会返回 403
                .csrf()
                .disable()
                .build();
        // CORS(跨域) 统一在网关配置
    }

    @Deprecated
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.addAllowedOriginPattern("*");
        cors.addAllowedHeader("*");
        cors.addAllowedMethod("*");
        cors.addExposedHeader("*");
        cors.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }

    /**
     * 若没有手动创建 persistent_logins 表
     * 则初次运行需要 setCreateTableOnStartup(true) 以创建该表
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(authorizationService)
                .and()
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        if (request.getRequestURI().endsWith("/login")) {
            response.getWriter().write(
                    new ObjectMapper().writeValueAsString(
                            ResponseEntity.success("登录成功！Login successful!"))
            );
        } else if (request.getRequestURI().endsWith("/logout")) {
            response.getWriter().write(
                    new ObjectMapper().writeValueAsString(
                            ResponseEntity.success("登出成功！Logout successful!"))
            );
        }

    }

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(
                        ResponseEntity.failure(AUTH_FAILED, "用户名或密码错误！Login failed!"))
        );
    }

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(
                        ResponseEntity.failure(FORBIDDEN, "未授权的访问！Request failed!"))
        );
    }
}
