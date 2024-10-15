package com.esPublico.app.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esPublico.app.domain.helpers.JwtHelper;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

	private final JwtHelper jwtHelper;

	@GetMapping("/generate-token")
	public ResponseEntity<String> generateToken() {
		final String token = jwtHelper.createToken();
		return ResponseEntity.ok(token);
	}
}
