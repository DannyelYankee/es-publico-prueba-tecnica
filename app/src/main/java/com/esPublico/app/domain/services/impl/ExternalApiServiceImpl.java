package com.esPublico.app.domain.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.esPublico.app.domain.exceptions.ExternalApiException;
import com.esPublico.app.domain.models.Order;
import com.esPublico.app.domain.repositories.OrderRepository;
import com.esPublico.app.domain.services.ExternalApiService;
import com.esPublico.app.transfers.dtos.OrderPageDTO;
import com.esPublico.app.transfers.mappers.OrderMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class ExternalApiServiceImpl implements ExternalApiService {
	private final WebClient webClient;
	private final OrderRepository orderRepository;
	private final OrderMapper orderMapper;
	private static final int BATCH_SIZE = 1000;

	@Override
	public void fetchAndProcessRecords() {
		String nextPageUrl = "/orders?page=1&max-per-page=" + BATCH_SIZE;

		while (nextPageUrl != null) {
			try {
				final OrderPageDTO response = fetchOrdersFromApi(nextPageUrl);
				nextPageUrl = processResponse(response);
			} catch (final Exception e) {
				log.error("Error fetching records from API", e);
				throw new ExternalApiException("Error fetching records from API", e);
			}
		}
	}

	private OrderPageDTO fetchOrdersFromApi(String nextPageUrl) {
		return webClient.get().uri(nextPageUrl).retrieve().bodyToMono(OrderPageDTO.class).block();
	}

	private String processResponse(OrderPageDTO response) {
		if ((response != null) && !response.getContent().isEmpty()) {
			final List<Order> orders = orderMapper.toEntities(response.getContent());
			saveOrders(orders);
			return response.getLinks().getNext();
		}
		// finish if there's no more pages
		return null;
	}

	private void saveOrders(List<Order> orders) {
		try {
			orderRepository.saveAll(orders);
			log.info("Successfully saved batch of {} orders.", orders.size());
		} catch (final Exception e) {
			log.error("Error saving batch, proceeding with individual save.", e);
			saveOrdersIndividually(orders);
		}
	}

	private void saveOrdersIndividually(List<Order> orders) {
		orders.forEach(order -> {
			try {
				orderRepository.save(order);
			} catch (final Exception ex) {
				log.error("Failed to save order with UUID: {}", order.getUuid(), ex);
			}
		});
	}

}
