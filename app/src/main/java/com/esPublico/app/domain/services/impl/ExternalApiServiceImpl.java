package com.esPublico.app.domain.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.esPublico.app.domain.models.Order;
import com.esPublico.app.domain.repositories.OrderRepository;
import com.esPublico.app.domain.services.ExternalApiService;
import com.esPublico.app.transfers.dtos.OrderPageDTO;
import com.esPublico.app.transfers.dtos.PageLinksDTO;
import com.esPublico.app.transfers.mappers.OrderMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
public class ExternalApiServiceImpl implements ExternalApiService {
	private final WebClient webClient;
	private final OrderRepository orderRepository;
	private final OrderMapper orderMapper;
	private static final int BATCH_SIZE = 1000;
	private static final int MAX_CONCURRENCY = 10;

	@Override
	public void fetchAndProcessRecords() {
		final String initialUrl = "/orders?page=1&max-per-page=" + BATCH_SIZE;

		Flux.just(initialUrl).expand(this::getNextPageUrl).flatMap(this::fetchOrdersFromApi, MAX_CONCURRENCY)
				.flatMap(this::processResponse, MAX_CONCURRENCY)
				.onErrorContinue((e, url) -> log.error("Error fetching records from API: {}", url, e)).blockLast();
	}

	private Mono<String> getNextPageUrl(String currentPageUrl) {
		return Mono.justOrEmpty(currentPageUrl)
				.flatMap(url -> webClient.get().uri(url).retrieve().bodyToMono(OrderPageDTO.class))
				.map(OrderPageDTO::getLinks).map(PageLinksDTO::getNext).switchIfEmpty(Mono.empty());
	}

	private Mono<OrderPageDTO> fetchOrdersFromApi(String pageUrl) {
		return webClient.get().uri(pageUrl).retrieve().bodyToMono(OrderPageDTO.class);
	}

	private Mono<Void> processResponse(OrderPageDTO response) {
		if ((response != null) && !response.getContent().isEmpty()) {
			final List<Order> orders = orderMapper.toEntities(response.getContent());
			return saveOrders(orders).then();
		}
		return Mono.empty();
	}

	private Mono<Void> saveOrders(List<Order> orders) {
		return Mono.fromRunnable(() -> {
			try {
				orderRepository.saveAll(orders);
				log.info("Successfully saved batch of {} orders.", orders.size());
			} catch (final Exception e) {
				log.error("Error saving batch, proceeding with individual save.", e);
				saveOrdersIndividually(orders);
			}
		});
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
