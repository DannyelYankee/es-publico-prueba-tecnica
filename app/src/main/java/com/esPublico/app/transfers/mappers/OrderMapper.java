package com.esPublico.app.transfers.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.esPublico.app.domain.models.Order;
import com.esPublico.app.transfers.dtos.OrderDTO;
import com.esPublico.app.utils.DateUtils;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class OrderMapper {
	private final DateUtils dateUtils;

	public List<Order> toEntities(List<OrderDTO> dtos) {
		return dtos.stream().map(this::toEntity).collect(Collectors.toList());
	}

	public Order toEntity(OrderDTO dto) {
		final Order order = new Order();
		order.setUuid(dto.getUuid());
		order.setId(Long.valueOf(dto.getId()));
		order.setRegion(dto.getRegion());
		order.setCountry(dto.getCountry());
		order.setItemType(dto.getItemType());
		order.setSalesChannel(dto.getSalesChannel());
		order.setPriority(dto.getPriority());
		order.setOrderDate(dateUtils.parseStringToLocalDate(dto.getDate()));
		order.setShipDate(dateUtils.parseStringToLocalDate(dto.getShipDate()));
		order.setUnitsSold(dto.getUnitsSold());
		order.setUnitPrice(dto.getUnitPrice());
		order.setUnitCost(dto.getUnitCost());
		order.setTotalRevenue(dto.getTotalRevenue());
		order.setTotalCost(dto.getTotalCost());
		order.setTotalProfit(dto.getTotalProfit());
		order.setSelfLink(dto.getLinks().getSelf());
		return order;
	}

}
