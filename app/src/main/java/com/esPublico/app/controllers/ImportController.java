package com.esPublico.app.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esPublico.app.domain.exceptions.ExternalApiException;
import com.esPublico.app.domain.services.ImportService;
import com.esPublico.app.domain.services.OrderService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "import")
@AllArgsConstructor
@Slf4j
public class ImportController {

	private final ImportService importService;
	private final OrderService orderService;

	@PostMapping("/orders")
	public ResponseEntity<?> importAndResumeOrders() {
		try {
			importService.importOrders();
			final var summary = orderService.getOrderSummary();

			log.info("importAndResumeOrders finished successfully");
			return ResponseEntity.ok(summary);

		} catch (final ExternalApiException e) {

			log.error("Failed to import and resume orders", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to import orders: " + e.getMessage());

		}
	}
}
