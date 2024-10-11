package com.esPublico.app.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esPublico.app.domain.services.OrderService;
import com.esPublico.app.transfers.dtos.SummaryDTO;

@RestController
@RequestMapping(path = "orders")
public class OrderController {

	private final OrderService orderService;

	@Autowired
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@GetMapping("/summary")
	public ResponseEntity<Map<String, List<SummaryDTO>>> getOrderSummary() {
		return ResponseEntity.ok(orderService.getOrderSummary());
	}
}
