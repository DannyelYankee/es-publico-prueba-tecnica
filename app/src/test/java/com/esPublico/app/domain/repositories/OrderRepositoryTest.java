package com.esPublico.app.domain.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.esPublico.app.domain.models.Order;

@RunWith(SpringRunner.class)
@DataJpaTest
public class OrderRepositoryTest {

	@Autowired
	private OrderRepository orderRepository;

	private Order createOrder(String uuid, Long id) {
		final Order order = new Order();
		order.setUuid(uuid);
		order.setId(id);
		order.setRegion("North America");
		order.setCountry("USA");
		order.setItemType("Electronics");
		order.setSalesChannel("Online");
		order.setPriority('H');
		order.setOrderDate(LocalDate.of(2023, 10, 13));
		order.setShipDate(LocalDate.of(2023, 10, 20));
		order.setUnitsSold(100);
		order.setUnitPrice(new BigDecimal("299.99"));
		order.setUnitCost(new BigDecimal("199.99"));
		order.setTotalRevenue(new BigDecimal("29999.00"));
		order.setTotalCost(new BigDecimal("19999.00"));
		order.setTotalProfit(new BigDecimal("10000.00"));
		order.setSelfLink("selfLink");
		return order;
	}

	@Test
	public void testSaveOrder() {

		final Order order = createOrder(UUID.randomUUID().toString(), 1L);

		final Order savedOrder = orderRepository.save(order);

		assertNotNull(savedOrder);
		assertEquals(order.getUuid(), savedOrder.getUuid());
		assertEquals(Long.valueOf(1L), savedOrder.getId());
		assertEquals("North America", savedOrder.getRegion());
	}

	@Test
	public void testUpdateOrder() {

		final Order order = createOrder(UUID.randomUUID().toString(), 1L);
		orderRepository.save(order);

		order.setCountry("Canada");
		final Order updatedOrder = orderRepository.save(order);

		assertNotNull(updatedOrder);
		assertEquals("Canada", updatedOrder.getCountry());

		final Order foundOrder = orderRepository.findById(order.getUuid()).orElse(null);
		assertNotNull(foundOrder);
		assertEquals("Canada", foundOrder.getCountry());
	}

	@Test
	public void testDeleteOrder() {

		final Order order = createOrder(UUID.randomUUID().toString(), 1L);
		orderRepository.save(order);

		orderRepository.delete(order);

		assertFalse(orderRepository.findById(order.getUuid()).isPresent());
	}

	@Test
	public void testFindAllByOrderByIdAsc() {

		final Order order1 = createOrder(UUID.randomUUID().toString(), 1L);
		final Order order2 = createOrder(UUID.randomUUID().toString(), 2L);
		orderRepository.save(order1);
		orderRepository.save(order2);

		final List<Order> orders = orderRepository.findAllByOrderByIdAsc();

		assertNotNull(orders);
		assertEquals(2, orders.size());
		assertEquals(Long.valueOf(1L), orders.get(0).getId());
		assertEquals(Long.valueOf(2L), orders.get(1).getId());
	}

}
