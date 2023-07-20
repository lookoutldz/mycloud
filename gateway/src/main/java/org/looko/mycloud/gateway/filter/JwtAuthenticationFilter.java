package org.looko.mycloud.gateway.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.looko.mycloud.commonstarter.entity.ResponseEntity;
import org.looko.mycloud.gateway.util.JwtUtil;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.looko.mycloud.commonstarter.constant.AuthConstants.AUTH_HEADER_KEY;
import static org.looko.mycloud.commonstarter.constant.AuthConstants.TOKEN_PREFIX;
import static org.looko.mycloud.commonstarter.enumeration.BasicResponseStatus.AUTH_FAILED;

@Slf4j
public class JwtAuthenticationFilter implements WebFilter {
    private static final Set<Predicate<String>> whitePatterns;
    static {
        Set<String> whiteRegexes = Set.of("/resources/**",  "/auth/validcode/**", "/auth/resetPassword/**",
                "/login", "/auth/register", "/user/test/port");
        whitePatterns = new HashSet<>();
        for (String regex : whiteRegexes) {
            regex = regex.replaceAll("\\*", "\\\\*");
            whitePatterns.add(Pattern.compile(regex).asMatchPredicate());
        }
    }

    @Override
    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        if (whitePatterns.stream().anyMatch(p -> p.test(path))) {
            log.info("白名单[" + path + "]，跳过 jwt 校验");
            return chain.filter(exchange);
        }

        String tokenHolder = exchange.getRequest().getHeaders().getFirst(AUTH_HEADER_KEY);
        String message = null;
        if (tokenHolder != null && tokenHolder.startsWith(TOKEN_PREFIX)) {
            String jwtToken = tokenHolder.replace(TOKEN_PREFIX, "");
            try {
                String tokenInfo = JwtUtil.verifyToken(jwtToken);
                log.info("Token 校验通过. Path[{}], token: {}", path, tokenInfo);
                return chain.filter(exchange);
            } catch (TokenExpiredException e) {
                message = "token 已失效，请重新登录!";
            } catch (JWTVerificationException e) {
                message = "token 验证失败!";
            }
        }
        ServerHttpResponse response = exchange.getResponse();
        return response.writeWith(
                Mono.just(response.bufferFactory().wrap(ResponseEntity.failure(AUTH_FAILED, message).toJson().getBytes(UTF_8))));
    }
}
