package com.esPublico.app.domain.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.esPublico.app.domain.models.Order;
import com.esPublico.app.domain.repositories.OrderRepository;
import com.esPublico.app.domain.services.OrderService;
import com.esPublico.app.transfers.dtos.SummaryDTO;
import com.esPublico.app.utils.ScriptExecuter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
	private final JdbcTemplate jdbcTemplate;
	private final OrderRepository orderRepository;
	private final ScriptExecuter scriptExecuter;

	@Override
	public void dropIndexes() {
		try {
			scriptExecuter.executeScript("drop_indexes.sql");
			log.info("OrderService.dropIndexes executed successfully");
		} catch (final Exception e) {
			log.error("Error dropping indexes: {}", e.getMessage(), e);
		}
	}

	@Override
	public void createIndexes() {
		try {
			scriptExecuter.executeScript("create_indexes.sql");
			log.info("OrderService.createIndexes executed successfully");
		} catch (final Exception e) {
			log.error("Error creating indexes: {}", e.getMessage(), e);
		}
	}

	@Override
	public List<Order> getOrdersSortedById() {
		return orderRepository.findAllByOrderByIdAsc();
	}

	@Override
	public void insertOrders(List<Order> orders) {
		try {
			orderRepository.saveAll(orders);
			log.info("Successfully saved batch of {} orders.", orders.size());
		} catch (final Exception e) {
			log.error("Error saving batch of orders, proceeding with individual save.", e);
			insertOrdersIndividually(orders);
		}
	}

	private void insertOrdersIndividually(List<Order> orders) {
		orders.forEach(order -> {
			try {
				orderRepository.save(order);
			} catch (final Exception ex) {
				log.error("Failed to save order with UUID: {}", order.getUuid(), ex);
			}
		});
	}

	@Override
	public Map<String, List<SummaryDTO>> getOrderSummary() {
		final String sql = "SELECT 'region' AS category, region AS value, COUNT(*) AS total FROM orders GROUP BY region "
				+ "UNION ALL SELECT 'country' AS category, country AS value, COUNT(*) AS total FROM orders GROUP BY country "
				+ "UNION ALL SELECT 'item_type' AS category, item_type AS value, COUNT(*) AS total FROM orders GROUP BY item_type "
				+ "UNION ALL SELECT 'sales_channel' AS category, sales_channel AS value, COUNT(*) AS total FROM orders GROUP BY sales_channel "
				+ "UNION ALL SELECT 'priority' AS category, priority AS value, COUNT(*) AS total FROM orders GROUP BY priority";

		final List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

		log.info("SQl {} executed succesfully", sql);

		final Map<String, List<SummaryDTO>> groupedResults = new HashMap<>();

		for (final Map<String, Object> row : results) {
			final String category = (String) row.get("category");
			final String value = (String) row.get("value");
			final Long total = ((Number) row.get("total")).longValue();

			groupedResults.computeIfAbsent(category, summarylist -> new ArrayList<>());

			groupedResults.get(category).add(new SummaryDTO(value, total));
		}

		log.info("getOrderSummary finished successfully");
		return groupedResults;
	}

}
