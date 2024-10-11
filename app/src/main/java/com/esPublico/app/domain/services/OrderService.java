package com.esPublico.app.domain.services;

import java.util.List;
import java.util.Map;

import com.esPublico.app.domain.models.Order;
import com.esPublico.app.transfers.dtos.SummaryDTO;

public interface OrderService {
	public void dropIndexes();

	public void createIndexes();

	public void insertOrders(List<Order> orders);

	public List<Order> getOrdersSortedById();

	public Map<String, List<SummaryDTO>> getOrderSummary();
}
