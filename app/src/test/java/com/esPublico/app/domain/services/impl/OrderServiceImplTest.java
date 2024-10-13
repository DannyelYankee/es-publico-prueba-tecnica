package com.esPublico.app.domain.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.esPublico.app.domain.models.Order;
import com.esPublico.app.domain.repositories.OrderRepository;
import com.esPublico.app.transfers.dtos.SummaryDTO;
import com.esPublico.app.utils.ScriptExecuter;

public class OrderServiceImplTest {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private ScriptExecuter scriptExecuter;

	@Mock
	private JdbcTemplate jdbcTemplate;

	@InjectMocks
	private OrderServiceImpl orderService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testDropIndexesSuccess() throws Exception {

		orderService.dropIndexes();

		verify(scriptExecuter, times(1)).executeScript("drop_indexes.sql");
	}

	@Test
	public void testDropIndexesFailure() throws Exception {

		doThrow(new RuntimeException("Script execution failed")).when(scriptExecuter).executeScript("drop_indexes.sql");

		orderService.dropIndexes();

		verify(scriptExecuter, times(1)).executeScript("drop_indexes.sql");
	}

	@Test
	public void testCreateIndexesSuccess() throws Exception {

		orderService.createIndexes();

		verify(scriptExecuter, times(1)).executeScript("create_indexes.sql");
	}

	@Test
	public void testCreateIndexesFailure() throws Exception {

		doThrow(new RuntimeException("Script execution failed")).when(scriptExecuter)
				.executeScript("create_indexes.sql");

		orderService.createIndexes();

		verify(scriptExecuter, times(1)).executeScript("create_indexes.sql");
	}

	@Test
	public void testGetOrdersSortedById() {

		final Order order1 = new Order();
		order1.setId(1L);

		final Order order2 = new Order();
		order2.setId(2L);

		final List<Order> mockOrders = Arrays.asList(order1, order2);

		when(orderRepository.findAllByOrderByIdAsc()).thenReturn(mockOrders);

		final List<Order> result = orderService.getOrdersSortedById();

		verify(orderRepository, times(1)).findAllByOrderByIdAsc();
		assertEquals(2, result.size());
		assertEquals(Long.valueOf(1L), result.get(0).getId());
		assertEquals(Long.valueOf(2L), result.get(1).getId());
	}

	@Test
	public void testInsertOrdersFailure() {

		final List<Order> orders = Arrays.asList(new Order(), new Order());

		doThrow(new RuntimeException("Batch save failed")).when(orderRepository).saveAll(orders);

		orderService.insertOrders(orders);

		verify(orderRepository, times(1)).saveAll(orders);

		verify(orderRepository, times(2)).save(any(Order.class));
	}

	@Test
	public void testInsertOrdersSuccess() {

		final List<Order> orders = Arrays.asList(new Order(), new Order());

		orderService.insertOrders(orders);

		verify(orderRepository, times(1)).saveAll(orders);
	}

	@Test
	public void testGetOrderSummary() {

		final List<Map<String, Object>> mockResults = new ArrayList<>();

		final Map<String, Object> row1 = new HashMap<>();
		row1.put("category", "region");
		row1.put("value", "North America");
		row1.put("total", 10L);

		final Map<String, Object> row2 = new HashMap<>();
		row2.put("category", "country");
		row2.put("value", "USA");
		row2.put("total", 5L);

		mockResults.add(row1);
		mockResults.add(row2);

		when(jdbcTemplate.queryForList(anyString())).thenReturn(mockResults);

		final Map<String, List<SummaryDTO>> result = orderService.getOrderSummary();

		verify(jdbcTemplate, times(1)).queryForList(anyString());

		assertNotNull(result);
		assertEquals(2, result.size());

		assertTrue(result.containsKey("region"));
		assertEquals(1, result.get("region").size());
		assertEquals("North America", result.get("region").get(0).getValue());
		assertEquals(Long.valueOf(10L), result.get("region").get(0).getTotal());

		assertTrue(result.containsKey("country"));
		assertEquals(1, result.get("country").size());
		assertEquals("USA", result.get("country").get(0).getValue());
		assertEquals(Long.valueOf(5L), result.get("country").get(0).getTotal());
	}

}
