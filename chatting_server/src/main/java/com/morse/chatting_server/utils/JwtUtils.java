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
	private String ACCESS_SECRET_KEY;
	private final String USER_IDX = "user_idx";

	JWTVerifier jwtVerifier;
	ResponseMessage MESSAGE;

	@PostConstruct
	private void init() {
		jwtVerifier = JWT.require(Algorithm.HMAC256(ACCESS_SECRET_KEY)).build();
	}

	public Long isValidateToken(String token) throws JwtException {

		try {
			jwtVerifier.verify(token);
			return JWT.decode(token).getClaim(USER_IDX).asLong();

		} catch (TokenExpiredException te) {
			log.error(te.getMessage());
			log.info(MESSAGE.EXPIRED_TOKEN);
			//throw new TokenExpiredException("토큰이 만료되었습니다.");
		} catch (SignatureVerificationException sve) {
			log.error(sve.getMessage());
			log.info(MESSAGE.MODULATE_TOKEN);
			//throw new SignatureVerificationException("토큰이 변조되었습니다.");
		} catch (JWTDecodeException jde) {
			log.error(jde.getMessage());
			log.info(MESSAGE.NOT_TOKEN_TYPE);
			//throw new JWTDecodeException("토큰의 유형이 아닙니다.");
		} catch (JwtException e) {
			log.error(e.getMessage());
			log.info(MESSAGE.JWT_EXCEPTION);
			//throw new JwtException("Jwt Exception");
		}
		return null;
	}
}
