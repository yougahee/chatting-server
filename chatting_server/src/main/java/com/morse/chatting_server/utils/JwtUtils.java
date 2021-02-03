package com.morse.chatting_server.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.morse.chatting_server.exception.SignatureVerificationException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret.at}")
    private static String ACCESS_SECRET_KEY = "gagajwtAccessToken";
    private final String USER_IDX = "user_idx";

    JWTVerifier jwtVerifier;

    @PostConstruct
    private void init() {
        jwtVerifier = JWT.require(Algorithm.HMAC256(ACCESS_SECRET_KEY)).build();
    }

    public long isValidateToken(String token) throws JwtException {
        log.info("validation 안에 들어온 token : " + token);
        try {
            //jwtVerifier.verify(token);
            return JWT.decode(token).getClaim(USER_IDX).asLong();
        } catch (TokenExpiredException te) {
            log.error(te.getMessage());
            throw new TokenExpiredException("토큰이 만료되었습니다.");
        } catch (SignatureVerificationException sve) {
            log.error(sve.getMessage());
            throw new SignatureVerificationException("토큰이 변조되었습니다.");
        } catch (JWTDecodeException jde) {
            log.error(jde.getMessage());
            throw new JWTDecodeException("토큰의 유형이 아닙니다.");
        } catch (JwtException e) {
            log.error(e.getMessage());
            throw new JwtException("Jwt Exception");
        }
    }
}


