package com.esPublico.app.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esPublico.app.domain.exceptions.ExternalApiException;
import com.esPublico.app.domain.services.ExternalApiService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "import")
@AllArgsConstructor
@Slf4j
public class ImportController {

	private final ExternalApiService externalApiService;

	@PostMapping("/orders")
	public ResponseEntity<String> importOrders() {
		try {
			externalApiService.fetchAndProcessRecords();
			return ResponseEntity.ok("Orders imported successfully");
		} catch (final ExternalApiException e) {
			log.error("Failed to import orders", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to import orders: " + e.getMessage());
		}
	}
}
