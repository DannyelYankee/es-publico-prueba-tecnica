package com.esPublico.app.domain.services.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
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
import com.esPublico.app.utils.DateUtils;

public class CsvExportServiceImplTest {

	@Mock
	private DateUtils dateUtils;

	@InjectMocks
	private CsvExportServiceImpl csvExportService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testExportOrdersToCsv_Success() throws IOException {

		final Order order1 = createOrder(1L, 'H', LocalDate.of(2024, 10, 13), "Region1", "Country1", "ItemType1",
				"Online", LocalDate.of(2024, 10, 14), 100, new BigDecimal("50.00"), new BigDecimal("30.00"),
				new BigDecimal("5000.00"), new BigDecimal("3000.00"), new BigDecimal("2000.00"));
		final List<Order> orders = Arrays.asList(order1);

		when(dateUtils.formatLocalDate(any())).thenReturn("2024-10-13", "2024-10-14");

		final byte[] csvData = csvExportService.exportOrdersToCsv(orders);

		final String expectedCsv = "Order ID,Order Priority,Order Date,Region,Country,Item Type,Sales Channel,Ship Date,Units Sold,Unit Price,Unit Cost,Total Revenue,Total Cost,Total Profit\n"
				+ "1,H,2024-10-13,Region1,Country1,ItemType1,Online,2024-10-14,100,50.00,30.00,5000.00,3000.00,2000.00\n";
		assertEquals(expectedCsv, new String(csvData));
	}

	@Test
	public void testExportOrdersToCsv_EmptyOrderList() throws IOException {

		final List<Order> orders = Arrays.asList();

		final byte[] csvData = csvExportService.exportOrdersToCsv(orders);

		final String expectedCsv = "Order ID,Order Priority,Order Date,Region,Country,Item Type,Sales Channel,Ship Date,Units Sold,Unit Price,Unit Cost,Total Revenue,Total Cost,Total Profit\n";
		assertEquals(expectedCsv, new String(csvData));
	}

	@Test
	public void testExportOrdersToCsv_Exception() {

		final List<Order> orders = null;

		final byte[] csvData = csvExportService.exportOrdersToCsv(orders);

		assertEquals(0, csvData.length);
	}

	private Order createOrder(Long id, char priority, LocalDate orderDate, String region, String country,
			String itemType, String salesChannel, LocalDate shipDate, int unitsSold, BigDecimal unitPrice,
			BigDecimal unitCost, BigDecimal totalRevenue, BigDecimal totalCost, BigDecimal totalProfit) {
		final Order order = mock(Order.class);
		when(order.getId()).thenReturn(id);
		when(order.getPriority()).thenReturn(priority);
		when(order.getOrderDate()).thenReturn(orderDate);
		when(order.getRegion()).thenReturn(region);
		when(order.getCountry()).thenReturn(country);
		when(order.getItemType()).thenReturn(itemType);
		when(order.getSalesChannel()).thenReturn(salesChannel);
		when(order.getShipDate()).thenReturn(shipDate);
		when(order.getUnitsSold()).thenReturn(unitsSold);
		when(order.getUnitPrice()).thenReturn(unitPrice);
		when(order.getUnitCost()).thenReturn(unitCost);
		when(order.getTotalRevenue()).thenReturn(totalRevenue);
		when(order.getTotalCost()).thenReturn(totalCost);
		when(order.getTotalProfit()).thenReturn(totalProfit);
		return order;
	}
}
