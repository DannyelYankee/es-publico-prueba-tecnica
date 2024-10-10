package com.esPublico.app.transfers.dtos;

import java.util.List;

import lombok.Data;

@Data
public class OrderPageDTO {
	private int page;
	private List<OrderDTO> content;
	private PageLinksDTO links;

}
