package com.esPublico.app.domain.filters;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import com.esPublico.app.domain.helpers.JwtHelper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class AuthFilter extends OncePerRequestFilter {

	private final JwtHelper jwtHelper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authorizationHeader = request.getHeader("Authorization");

		if ((authorizationHeader != null) && authorizationHeader.startsWith("Bearer ")) {
			final String token = authorizationHeader.substring(7);

			try {
				if (!jwtHelper.validateToken(token)) {
					throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token expired or invalid");
				}
				final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(null,
						null, new ArrayList<>());
				SecurityContextHolder.getContext().setAuthentication(authentication);
				log.info("Token is valid and authentication set");
			} catch (final Exception e) {
				log.error("JWT token is invalid", e);
				response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid JWT token");
				return;
			}
		}

		filterChain.doFilter(request, response);
	}
}
