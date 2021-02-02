package com.morse.chatting_server.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.morse.chatting_server.exception.SignatureVerificationException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
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
	private final String USER_IDX = "user_idx";

	@PostConstruct
	protected void init() {
		ACCESS_SECRET_KEY = Base64.getEncoder().encodeToString(ACCESS_SECRET_KEY.getBytes());
	}

	public long isValidateToken(String token) throws JwtException {
		String key = ACCESS_SECRET_KEY;

		try {
			Jwts.parser()
					.setSigningKey(key)
					.parseClaimsJws(token);

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
