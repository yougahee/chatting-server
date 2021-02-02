package com.morse.chatting_server.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;

@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret.at}")
    private String ACCESS_SECRET_KEY;

    @PostConstruct
    protected void init() {
        ACCESS_SECRET_KEY = Base64.getEncoder().encodeToString(ACCESS_SECRET_KEY.getBytes());
    }

    /*public void isValidateToken(String token) throws JwtException{

        String key = ACCESS_SECRET_KEY;

        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token);

            log.info("토큰 validate");
        } catch (TokenExpiredException te) {
            log.error(te.getMessage());
            throw new TokenExpiredException("토큰이 만료되었습니다.");
        } catch (SignatureVerificationException sve) {
            log.error(sve.getMessage());
            throw new SignatureVerificationException("토큰이 변조되었습니다.");
        } catch (JWTDecodeException jde) {
            log.error(jde.getMessage());
            throw new JWTDecodeException("토큰의 유형이 아닙니다.");
        } catch (JwtException  e) {
            log.error(e.getMessage());
            throw new JwtException("Jwt Exception");
        }
    }*/
}
