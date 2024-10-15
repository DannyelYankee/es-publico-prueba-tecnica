package com.esPublico.app.domain.helpers;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtHelper {

	@Value("${token.secret_key}")
	private String jwtSecret;

	public String createToken() {
		final var now = new Date();
		final var expirationDate = new Date(now.getTime() + (30L * 24 * 60 * 60 * 1000)); // 30 days in milliseconds
		return Jwts.builder().setIssuedAt(now).setExpiration(expirationDate).signWith(getSecretKey()).compact();
	}

	public boolean validateToken(String token) {
		try {
			final var expirationDate = getExpirationDate(token);
			return expirationDate.after(new Date()); // Check if the token has expired
		} catch (final Exception e) {
			log.error("Invalid JWT token", e);
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Jwt invalid");
		}
	}

	private Date getExpirationDate(String token) {
		return getClaimsFromToken(token, Claims::getExpiration);
	}

	private <T> T getClaimsFromToken(String token, Function<Claims, T> resolver) {
		return resolver.apply(parseToken(token));
	}

	private Claims parseToken(String token) {
		return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token).getBody();
	}

	private SecretKey getSecretKey() {
		return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
	}
}
