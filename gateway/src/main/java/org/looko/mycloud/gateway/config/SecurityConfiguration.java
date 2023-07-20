package org.looko.mycloud.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.looko.mycloud.commonstarter.constant.AuthConstants;
import org.looko.mycloud.commonstarter.entity.ResponseEntity;
import org.looko.mycloud.gateway.filter.JwtAuthenticationFilter;
import org.looko.mycloud.gateway.filter.LogRequestPathFilter;
import org.looko.mycloud.gateway.service.AuthorizationService;
import org.looko.mycloud.gateway.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.looko.mycloud.commonstarter.enumeration.BasicResponseStatus.AUTH_FAILED;


@Slf4j
@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity httpSecurity,
            CorsConfigurationSource source,
            ReactiveAuthenticationManager reactiveAuthenticationManager,
            ServerAuthenticationSuccessHandler successHandler,
            ServerAuthenticationFailureHandler failureHandler,
            ServerLogoutHandler logoutHandler, ServerLogoutSuccessHandler logoutSuccessHandler) {
        log.info("执行 Spring Security 配置");
        httpSecurity
                .authorizeExchange((authorize) -> authorize
                        .pathMatchers("/resources/**",  "/auth/validcode/**", "/auth/resetPassword/**",
                                "/login", "/auth/register", "/user/test/port").permitAll()
                        .anyExchange().authenticated()
                )
                .formLogin(login -> login
                        .authenticationManager(reactiveAuthenticationManager)
                        .authenticationSuccessHandler(successHandler)
                        .authenticationFailureHandler(failureHandler)
                )
                .logout(logout -> logout
                        .logoutHandler(logoutHandler)
                        .logoutSuccessHandler(logoutSuccessHandler)
                        .logoutUrl("/auth/logout")
                )
                .addFilterAt(new LogRequestPathFilter(), SecurityWebFiltersOrder.FIRST)
                .addFilterAt(new JwtAuthenticationFilter(), SecurityWebFiltersOrder.AUTHORIZATION)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(corsSpec -> corsSpec.configurationSource(source));
        return httpSecurity.build();
    }

    /**
     * 登录校验
     * @param authorizationService Service
     */
    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(AuthorizationService authorizationService, BCryptPasswordEncoder encoder) {
        return (authentication) -> {
            log.info("{} 正在登录校验", authentication.getPrincipal());
            String username = authentication.getName();
            String rawPassword = authentication.getCredentials().toString();
            Mono<UserDetails> userDetailsMono = authorizationService.findByUsername(username);
            return userDetailsMono
                    .flatMap(userDetails -> {
                        if (userDetails != null
                                && username.equals(userDetails.getUsername())
                                && encoder.matches(rawPassword, userDetails.getPassword())) {
                            log.info("validate: userDetails.password={}, password={}", userDetails.getPassword(), rawPassword);
                            return Mono.just(UsernamePasswordAuthenticationToken
                                    .authenticated(userDetails, rawPassword, userDetails.getAuthorities()));
                        } else {
                            return Mono.error(new AuthenticationException("校验失败") {});
                        }
                    });
        };
    }


    /**
     * 登录成功处理器
     */
    @Bean
    public ServerAuthenticationSuccessHandler authenticationSuccessHandler() {
        return (webFilterExchange, authentication) -> {
            log.info("{} 登录成功", authentication.getName());
            // 生成JWT令牌
            String token = JwtUtil.generateToken(authentication.getName());
            ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
            // 返回登录成功的结果，可以包含令牌等信息
            response.getHeaders().add(AuthConstants.AUTH_HEADER_KEY, token);
            return response.writeWith(
                    Mono.just(response.bufferFactory().wrap(ResponseEntity.success("登录成功").toJson().getBytes(UTF_8))));
        };
    }

    /**
     * 登录失败处理器
     */
    @Bean
    public ServerAuthenticationFailureHandler authenticationFailureHandler() {
        return (webFilterExchange, exception) -> {
            log.info("登录失败处理");
            ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
            return response.writeWith(
                    Mono.just(response.bufferFactory().wrap(ResponseEntity.failure(AUTH_FAILED, "用户名或密码错误").toJson().getBytes(UTF_8))));
        };
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 登出处理器
     */
    @Bean
    public ServerLogoutHandler logoutHandler() {
        return (webFilterExchange, authentication) -> {
            log.info("{} 正在登出.", authentication.getName());
            SecurityContextHolder.clearContext();
            return Mono.empty();
        };
    }

    @Bean
    public ServerLogoutSuccessHandler logoutSuccessHandler() {
        return (webFilterExchange, authentication) -> {
            ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
            return response.writeWith(
                    Mono.just(response.bufferFactory().wrap(ResponseEntity.success("登出成功").toJson().getBytes(UTF_8))));
        };

    }

    /**
     *  Security 跨域处理
     * @return 跨域配置对象
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 允许所有头请求
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        // 允许所有域请求
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        // 允许所有方法请求
        configuration.setAllowedMethods(Collections.singletonList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 允许所有地址请求
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
