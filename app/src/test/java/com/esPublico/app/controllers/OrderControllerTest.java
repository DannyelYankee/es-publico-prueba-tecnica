package com.esPublico.app.controllers;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.esPublico.app.domain.models.Order;
import com.esPublico.app.domain.services.CsvExportService;
import com.esPublico.app.domain.services.OrderService;
import com.esPublico.app.transfers.dtos.SummaryDTO;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

	@Mock
	private OrderService orderService;

	@Mock
	private CsvExportService csvExportService;

	@InjectMocks
	private OrderController orderController;

	private MockMvc mockMvc;

	@Before
	public void setUp() {

		mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
	}

	@Test
	public void testGetOrderSummary_Success() throws Exception {

		final Map<String, List<SummaryDTO>> mockSummary = new HashMap<>();
		mockSummary.put("summary", Collections.emptyList());

		when(orderService.getOrderSummary()).thenReturn(mockSummary);

		mockMvc.perform(get("/orders/summary")).andExpect(status().isOk()).andExpect(content().json("{'summary':[]}"));

		verify(orderService, times(1)).getOrderSummary();
	}

	@Test
	public void testExportOrders_Success() throws Exception {

		final List<Order> mockOrders = Collections.emptyList();
		final byte[] mockCsvBytes = "id,name\n1,Test Order".getBytes();

		when(orderService.getOrdersSortedById()).thenReturn(mockOrders);
		when(csvExportService.exportOrdersToCsv(mockOrders)).thenReturn(mockCsvBytes);

		mockMvc.perform(get("/orders/export")).andExpect(status().isOk()).andExpect(content().contentType("text/csv"))
				.andExpect(content().bytes(mockCsvBytes));

		verify(orderService, times(1)).getOrdersSortedById();
		verify(csvExportService, times(1)).exportOrdersToCsv(mockOrders);
	}

	@Test
	public void testExportOrders_Failure() throws Exception {

		when(orderService.getOrdersSortedById()).thenThrow(new RuntimeException("Error inesperado"));

		mockMvc.perform(get("/orders/export")).andExpect(status().isInternalServerError())

				.andExpect(content().string("An unexpected error occurred: Error inesperado"));

		verify(orderService, times(1)).getOrdersSortedById();
		verify(csvExportService, never()).exportOrdersToCsv(anyList());
	}
}
