package com.esPublico.app.domain.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.esPublico.app.domain.models.Order;
import com.esPublico.app.domain.services.ExternalApiService;
import com.esPublico.app.domain.services.OrderService;
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
	private final OrderMapper orderMapper;
	private final OrderService orderService;
	private static final int BATCH_SIZE = 1000;

	// Calculates the number of threads based on 83.33% of the available cores
	private static final int MAX_CONCURRENCY = (int) Math.ceil(Runtime.getRuntime().availableProcessors() * 0.833);

	@Override
	public void fetchAndProcessOrders() {
		final String initialUrl = "/orders?page=1&max-per-page=" + BATCH_SIZE;

		Flux.just(initialUrl).expand(this::getNextPageUrl).flatMap(this::fetchOrdersFromApi, MAX_CONCURRENCY)
				.flatMap(this::processResponse, MAX_CONCURRENCY)
				.onErrorContinue((e, url) -> log.error("Error fetching records from API: {}", url, e)).blockLast();

		log.info("fetchAndProcessOrders finished successfully");
	}

	private Mono<String> getNextPageUrl(String currentPageUrl) {
		log.info("get page {}", currentPageUrl);
		return Mono.justOrEmpty(currentPageUrl)
				.flatMap(url -> webClient.get().uri(url).retrieve().bodyToMono(OrderPageDTO.class))
				.map(OrderPageDTO::getLinks).map(PageLinksDTO::getNext).onErrorResume(e -> {
					log.error("Error fetching page at URL: {}", currentPageUrl, e);
					return Mono.empty();
				}).switchIfEmpty(Mono.empty());
	}

	private Mono<OrderPageDTO> fetchOrdersFromApi(String pageUrl) {

		return webClient.get().uri(pageUrl).retrieve().bodyToMono(OrderPageDTO.class);

	}

	private Mono<Void> processResponse(OrderPageDTO response) {

		if ((response != null) && !response.getContent().isEmpty()) {

			final List<Order> orders = orderMapper.toEntities(response.getContent());
			return Mono.fromRunnable(() -> orderService.insertOrders(orders)).then();

		}
		log.info("response is empty, {}", response);
		return Mono.empty();
	}
}
