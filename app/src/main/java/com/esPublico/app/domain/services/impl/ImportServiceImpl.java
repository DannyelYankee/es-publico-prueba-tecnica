package com.esPublico.app.domain.services.impl;

import org.springframework.stereotype.Service;

import com.esPublico.app.domain.services.ExternalApiService;
import com.esPublico.app.domain.services.ImportService;
import com.esPublico.app.domain.services.OrderService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class ImportServiceImpl implements ImportService {
	private final ExternalApiService externalApiService;
	private final OrderService orderService;

	@Override
	public void importOrders() {
		try {

			orderService.dropIndexes();
			log.info("Indexes dropped successfully");

			externalApiService.fetchAndProcessOrders();

			orderService.createIndexes();
			log.info("Indexes created successfully");

		} catch (final Exception e) {
			log.error("Error during order import process: {}", e.getMessage(), e);
		}
	}
}
