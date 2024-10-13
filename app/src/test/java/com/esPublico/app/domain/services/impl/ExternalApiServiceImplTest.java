package com.esPublico.app.domain.services.impl;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.esPublico.app.domain.models.Order;
import com.esPublico.app.domain.services.OrderService;
import com.esPublico.app.transfers.dtos.OrderDTO;
import com.esPublico.app.transfers.dtos.OrderPageDTO;
import com.esPublico.app.transfers.mappers.OrderMapper;

import reactor.core.publisher.Mono;

@RunWith(MockitoJUnitRunner.class)
public class ExternalApiServiceImplTest {

	@Mock
	private WebClient webClient;

	@Mock
	private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

	@Mock
	private WebClient.RequestHeadersSpec requestHeadersSpec;

	@Mock
	private WebClient.ResponseSpec responseSpec;

	@Mock
	private OrderMapper orderMapper;

	@Mock
	private OrderService orderService;

	@InjectMocks
	private ExternalApiServiceImpl externalApiService;

	@Test
	public void testFetchAndProcessOrders_Success() {

		final OrderPageDTO mockOrderPageDTO = new OrderPageDTO();
		mockOrderPageDTO.setContent(List.of(new OrderDTO()));

		when(webClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
		when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.bodyToMono(OrderPageDTO.class)).thenReturn(Mono.just(mockOrderPageDTO));

		when(orderMapper.toEntities(anyList())).thenReturn(List.of(new Order()));
		doNothing().when(orderService).insertOrders(anyList());

		externalApiService.fetchAndProcessOrders();

		verify(orderService, times(1)).insertOrders(anyList());
		verify(webClient, atLeastOnce()).get();
		verify(orderMapper, times(1)).toEntities(anyList());
	}

	@Test
	public void testFetchAndProcessOrders_ApiError() {

		when(webClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
		when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.bodyToMono(OrderPageDTO.class))
				.thenReturn(Mono.error(new WebClientResponseException(500, "Error", null, null, null)));

		externalApiService.fetchAndProcessOrders();

		verify(orderService, never()).insertOrders(anyList());
	}

	@Test
	public void testFetchAndProcessOrders_EmptyResponse() {

		final OrderPageDTO mockEmptyOrderPageDTO = new OrderPageDTO();
		mockEmptyOrderPageDTO.setContent(Collections.emptyList());

		when(webClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
		when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.bodyToMono(OrderPageDTO.class)).thenReturn(Mono.just(mockEmptyOrderPageDTO));

		externalApiService.fetchAndProcessOrders();

		verify(orderService, never()).insertOrders(anyList());
	}
}
