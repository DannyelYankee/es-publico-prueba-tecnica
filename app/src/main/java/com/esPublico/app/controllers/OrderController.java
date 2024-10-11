package com.esPublico.app.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esPublico.app.domain.services.CsvExportService;
import com.esPublico.app.domain.services.OrderService;
import com.esPublico.app.transfers.dtos.SummaryDTO;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "orders")
@AllArgsConstructor
public class OrderController {

	private final OrderService orderService;
	private final CsvExportService csvExportService;

	@GetMapping("/summary")
	public ResponseEntity<Map<String, List<SummaryDTO>>> getOrderSummary() {
		return ResponseEntity.ok(orderService.getOrderSummary());
	}

	@GetMapping("/export")
	public ResponseEntity<byte[]> exportOrders() {
		try {
			final var orders = orderService.getOrdersSortedById();
			final var csvBytes = csvExportService.exportOrdersToCsv(orders);

			return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=\"orders.csv\"")
					.contentType(MediaType.parseMediaType("text/csv")).body(csvBytes);
		} catch (final Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(("An unexpected error occurred: " + e.getMessage()).getBytes());
		}
	}

}
