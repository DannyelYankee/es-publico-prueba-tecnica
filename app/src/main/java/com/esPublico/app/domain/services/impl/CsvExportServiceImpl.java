package com.esPublico.app.domain.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.esPublico.app.domain.models.Order;
import com.esPublico.app.domain.services.CsvExportService;
import com.esPublico.app.utils.DateUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class CsvExportServiceImpl implements CsvExportService {
	private final DateUtils dateUtils;

	@Override
	public byte[] exportOrdersToCsv(List<Order> orders) {
		log.info("Starts exportOrdersToCsv");
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				PrintWriter writer = new PrintWriter(outputStream)) {

			writer.println(
					"Order ID,Order Priority,Order Date,Region,Country,Item Type,Sales Channel,Ship Date,Units Sold,Unit Price,Unit Cost,Total Revenue,Total Cost,Total Profit");

			for (final Order order : orders) {
				writer.println(orderToCsv(order));
			}

			writer.flush();
			log.info("exportation finished");
			return outputStream.toByteArray();
		} catch (final IOException e) {
			log.error("Error during the csv exportation", e);
			return new byte[0];
		} catch (final Exception e) {
			log.error("Error during the csv exportation", e);
			return new byte[0];
		}
	}

	private String orderToCsv(Order order) {
		return String.join(",", String.valueOf(order.getId()), String.valueOf(order.getPriority()),
				dateUtils.formatLocalDate(order.getOrderDate()), order.getRegion() != null ? order.getRegion() : "",
				order.getCountry() != null ? order.getCountry() : "",
				order.getItemType() != null ? order.getItemType() : "",
				order.getSalesChannel() != null ? order.getSalesChannel() : "",
				dateUtils.formatLocalDate(order.getShipDate()), String.valueOf(order.getUnitsSold()),
				order.getUnitPrice() != null ? order.getUnitPrice().toString() : "0.00",
				order.getUnitCost() != null ? order.getUnitCost().toString() : "0.00",
				order.getTotalRevenue() != null ? order.getTotalRevenue().toString() : "0.00",
				order.getTotalCost() != null ? order.getTotalCost().toString() : "0.00",
				order.getTotalProfit() != null ? order.getTotalProfit().toString() : "0.00");
	}

}
