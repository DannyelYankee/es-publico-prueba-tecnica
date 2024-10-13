package com.esPublico.app.transfers.mappers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.esPublico.app.domain.models.Order;
import com.esPublico.app.transfers.dtos.LinksDTO;
import com.esPublico.app.transfers.dtos.OrderDTO;
import com.esPublico.app.utils.DateUtils;

public class OrderMapperTest {

	@Mock
	private DateUtils dateUtils;

	@InjectMocks
	private OrderMapper orderMapper;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	private OrderDTO getOrderDTO() {
		final OrderDTO orderDTO = new OrderDTO();

		final String uuid = "123e4567-e89b-12d3-a456-426614174000";
		final String id = "1";
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

		links.setSelf("selfLink");
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

		return orderDTO;
	}

	@Test
	public void testToEntity() {
		// Given
		final OrderDTO orderDTO = getOrderDTO();

		final LocalDate orderDate = LocalDate.of(2023, 10, 13);
		final LocalDate shipDate = LocalDate.of(2023, 10, 20);

		// Mocking the date conversion
		when(dateUtils.parseStringToLocalDate("2023-10-13")).thenReturn(orderDate);
		when(dateUtils.parseStringToLocalDate("2023-10-20")).thenReturn(shipDate);

		// When
		final Order order = orderMapper.toEntity(orderDTO);

		// Then
		assertNotNull(order);
		assertEquals("123e4567-e89b-12d3-a456-426614174000", order.getUuid());
		assertEquals(Long.valueOf(orderDTO.getId()), order.getId());
		assertEquals(orderDTO.getRegion(), order.getRegion());
		assertEquals(orderDTO.getCountry(), order.getCountry());
		assertEquals(orderDTO.getItemType(), order.getItemType());
		assertEquals(orderDTO.getSalesChannel(), order.getSalesChannel());
		assertEquals(orderDTO.getPriority(), order.getPriority());
		assertEquals(orderDate, order.getOrderDate());
		assertEquals(shipDate, order.getShipDate());
		assertEquals(orderDTO.getUnitsSold(), order.getUnitsSold());
		assertEquals(orderDTO.getUnitPrice(), order.getUnitPrice());
		assertEquals(orderDTO.getUnitCost(), order.getUnitCost());
		assertEquals(orderDTO.getTotalRevenue(), order.getTotalRevenue());
		assertEquals(orderDTO.getTotalCost(), order.getTotalCost());
		assertEquals(orderDTO.getTotalProfit(), order.getTotalProfit());
		assertEquals(orderDTO.getLinks().getSelf(), order.getSelfLink());

		verify(dateUtils).parseStringToLocalDate("2023-10-13");
		verify(dateUtils).parseStringToLocalDate("2023-10-20");
	}

	@Test
	public void testToEntities() {

		final OrderDTO orderDTO1 = getOrderDTO();
		final OrderDTO orderDTO2 = getOrderDTO();
		orderDTO2.setUuid("223e4567-e89b-12d3-a456-426614174001");
		orderDTO2.setId("2");

		final List<OrderDTO> dtoList = Arrays.asList(orderDTO1, orderDTO2);

		final LocalDate orderDate1 = LocalDate.of(2023, 10, 13);
		final LocalDate shipDate1 = LocalDate.of(2023, 10, 20);

		final LocalDate orderDate2 = LocalDate.of(2023, 10, 13);
		final LocalDate shipDate2 = LocalDate.of(2023, 10, 20);

		when(dateUtils.parseStringToLocalDate("2023-10-13")).thenReturn(orderDate1).thenReturn(orderDate2);
		when(dateUtils.parseStringToLocalDate("2023-10-20")).thenReturn(shipDate1).thenReturn(shipDate2);

		final List<Order> orderList = orderMapper.toEntities(dtoList);

		assertNotNull(orderList);
		assertEquals(2, orderList.size());

		assertEquals(orderDTO1.getUuid(), orderList.get(0).getUuid());
		assertEquals(Long.valueOf(orderDTO1.getId()), orderList.get(0).getId());

		assertEquals(orderDTO2.getUuid(), orderList.get(1).getUuid());
		assertEquals(Long.valueOf(orderDTO2.getId()), orderList.get(1).getId());

		verify(dateUtils, times(2)).parseStringToLocalDate("2023-10-13");
		verify(dateUtils, times(2)).parseStringToLocalDate("2023-10-20");
	}

}
