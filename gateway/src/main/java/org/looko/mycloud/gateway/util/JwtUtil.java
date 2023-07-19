package org.looko.mycloud.gateway.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import java.util.Date;

import static org.looko.mycloud.commonstarter.constant.AuthConstants.TOKEN_PREFIX;
import static org.looko.mycloud.gateway.util.PropertyUtil.jwtExpireMillis;
import static org.looko.mycloud.gateway.util.PropertyUtil.jwtSecret;

public class JwtUtil {

    /**
     * 创建TOKEN
     * @param content content
     */
    public static String generateToken(String content){
        return TOKEN_PREFIX + JWT.create()
                .withSubject(content)
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpireMillis))
                .sign(Algorithm.HMAC512(jwtSecret));
    }

    /**
     * 验证token
     * @param token jwtToken
     */
    public static String verifyToken(String token) throws TokenExpiredException, JWTVerificationException {
        return JWT.require(Algorithm.HMAC512(jwtSecret))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""))
                .getSubject();
    }
}
