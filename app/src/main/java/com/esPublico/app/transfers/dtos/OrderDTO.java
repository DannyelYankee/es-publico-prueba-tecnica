package com.esPublico.app.transfers.dtos;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OrderDTO {
	private String uuid;

	@JsonProperty("id")
	private String id;

	private String region;
	private String country;

	@JsonProperty("item_type")
	private String itemType;

	@JsonProperty("sales_channel")
	private String salesChannel;

	private char priority;

	@JsonProperty("date")
	private String date;

	@JsonProperty("ship_date")
	private String shipDate;

	@JsonProperty("units_sold")
	private int unitsSold;

	@JsonProperty("unit_price")
	private BigDecimal unitPrice;

	@JsonProperty("unit_cost")
	private BigDecimal unitCost;

	@JsonProperty("total_revenue")
	private BigDecimal totalRevenue;

	@JsonProperty("total_cost")
	private BigDecimal totalCost;

	@JsonProperty("total_profit")
	private BigDecimal totalProfit;

	@JsonProperty("links")
	private LinksDTO links;
}
