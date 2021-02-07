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
	private static final String USER_IDX = "user_idx";

	JWTVerifier jwtVerifier;

	@PostConstruct
	private void init() {
		jwtVerifier = JWT.require(Algorithm.HMAC256(ACCESS_SECRET_KEY)).build();
	}

	public Long isValidateToken(String token) {

		try {
			jwtVerifier.verify(token);
			return JWT.decode(token).getClaim(USER_IDX).asLong();
		} catch (TokenExpiredException te) {
			log.error(te.getMessage());
			log.info(ResponseMessage.EXPIRED_TOKEN);
		} catch (SignatureVerificationException sve) {
			log.error(sve.getMessage());
			log.info(ResponseMessage.MODULATE_TOKEN);
		} catch (JWTDecodeException jde) {
			log.error(jde.getMessage());
			log.info(ResponseMessage.NOT_TOKEN_TYPE);
		} catch (JwtException e) {
			log.error(e.getMessage());
			log.info(ResponseMessage.JWT_EXCEPTION);
		}
		return null;
	}
}


