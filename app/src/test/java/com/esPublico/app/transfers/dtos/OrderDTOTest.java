package com.esPublico.app.transfers.dtos;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class OrderDTOTest {

	@Test
	public void testGettersAndSetters() {

		final OrderDTO orderDTO = new OrderDTO();

		final String uuid = "123e4567-e89b-12d3-a456-426614174000";
		final String id = "id";
		final String region = "region";
		final String country = "country";
		final String itemType = "item";
		final String salesChannel = "channel";
		final char priority = 'H';
		final String date = "2023-10-13";
		final String shipDate = "2023-10-20";
		final int unitsSold = 100;
		final BigDecimal unitPrice = new BigDecimal("299.99");
		final BigDecimal unitCost = new BigDecimal("199.99");
		final BigDecimal totalRevenue = new BigDecimal("29999.00");
		final BigDecimal totalCost = new BigDecimal("19999.00");
		final BigDecimal totalProfit = new BigDecimal("10000.00");
		final LinksDTO links = new LinksDTO();

		orderDTO.setUuid(uuid);
		orderDTO.setId(id);
		orderDTO.setRegion(region);
		orderDTO.setCountry(country);
		orderDTO.setItemType(itemType);
		orderDTO.setSalesChannel(salesChannel);
		orderDTO.setPriority(priority);
		orderDTO.setDate(date);
		orderDTO.setShipDate(shipDate);
		orderDTO.setUnitsSold(unitsSold);
		orderDTO.setUnitPrice(unitPrice);
		orderDTO.setUnitCost(unitCost);
		orderDTO.setTotalRevenue(totalRevenue);
		orderDTO.setTotalCost(totalCost);
		orderDTO.setTotalProfit(totalProfit);
		orderDTO.setLinks(links);

		assertEquals(uuid, orderDTO.getUuid());
		assertEquals(id, orderDTO.getId());
		assertEquals(region, orderDTO.getRegion());
		assertEquals(country, orderDTO.getCountry());
		assertEquals(itemType, orderDTO.getItemType());
		assertEquals(salesChannel, orderDTO.getSalesChannel());
		assertEquals(priority, orderDTO.getPriority());
		assertEquals(date, orderDTO.getDate());
		assertEquals(shipDate, orderDTO.getShipDate());
		assertEquals(unitsSold, orderDTO.getUnitsSold());
		assertEquals(unitPrice, orderDTO.getUnitPrice());
		assertEquals(unitCost, orderDTO.getUnitCost());
		assertEquals(totalRevenue, orderDTO.getTotalRevenue());
		assertEquals(totalCost, orderDTO.getTotalCost());
		assertEquals(totalProfit, orderDTO.getTotalProfit());
		assertEquals(links, orderDTO.getLinks());
	}
}
