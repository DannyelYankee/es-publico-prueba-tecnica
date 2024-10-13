package com.esPublico.app.domain.services.impl;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.esPublico.app.domain.services.ExternalApiService;
import com.esPublico.app.domain.services.OrderService;

public class ImportServiceImplTest {

	@Mock
	private ExternalApiService externalApiService;

	@Mock
	private OrderService orderService;

	@InjectMocks
	private ImportServiceImpl importService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testImportOrdersSuccess() {

		importService.importOrders();

		verify(orderService, times(1)).dropIndexes();
		verify(externalApiService, times(1)).fetchAndProcessOrders();
		verify(orderService, times(1)).createIndexes();
	}

	@Test
	public void testImportOrdersFailureInFetchAndProcess() {

		doThrow(new RuntimeException("API error")).when(externalApiService).fetchAndProcessOrders();

		importService.importOrders();

		verify(orderService, times(1)).dropIndexes();

		verify(externalApiService, times(1)).fetchAndProcessOrders();

		verify(orderService, never()).createIndexes();
	}
}
