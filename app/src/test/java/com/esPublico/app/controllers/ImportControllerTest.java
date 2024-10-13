package com.esPublico.app.controllers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.esPublico.app.domain.exceptions.ExternalApiException;
import com.esPublico.app.domain.services.ImportService;
import com.esPublico.app.domain.services.OrderService;
import com.esPublico.app.transfers.dtos.SummaryDTO;

@RunWith(MockitoJUnitRunner.class)
public class ImportControllerTest {

	@Mock
	private ImportService importService;

	@Mock
	private OrderService orderService;

	@InjectMocks
	private ImportController importController;

	private MockMvc mockMvc;

	@Before
	public void setUp() {

		mockMvc = MockMvcBuilders.standaloneSetup(importController).build();
	}

	@Test
	public void testImportAndResumeOrders_Success() throws Exception {

		final Map<String, List<SummaryDTO>> mockSummary = Collections.singletonMap("summary", Collections.emptyList());

		doNothing().when(importService).importOrders();
		when(orderService.getOrderSummary()).thenReturn(mockSummary);

		mockMvc.perform(post("/import/orders").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json("{'summary':[]}"));

		verify(importService, times(1)).importOrders();
		verify(orderService, times(1)).getOrderSummary();
	}

	@Test
	public void testImportAndResumeOrders_ApiError() throws Exception {

		final Throwable cause = new RuntimeException("Cause");
		final ExternalApiException apiException = new ExternalApiException("API error", cause);

		doThrow(apiException).when(importService).importOrders();

		mockMvc.perform(post("/import/orders").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(content().string("Failed to import orders: API error"));

		verify(importService, times(1)).importOrders();
		verify(orderService, never()).getOrderSummary();
	}
}
