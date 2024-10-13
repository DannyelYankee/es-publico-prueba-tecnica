package com.esPublico.app.transfers.dtos;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class OrderPageDTOTest {

	@Test
	public void testGettersAndSetters() {

		final OrderPageDTO orderPageDTO = new OrderPageDTO();
		final int page = 1;
		final List<OrderDTO> content = new ArrayList<>();
		final PageLinksDTO links = new PageLinksDTO();

		orderPageDTO.setPage(page);
		orderPageDTO.setContent(content);
		orderPageDTO.setLinks(links);

		assertEquals(page, orderPageDTO.getPage());
		assertEquals(content, orderPageDTO.getContent());
		assertEquals(links, orderPageDTO.getLinks());
	}
}
