package com.esPublico.app.transfers.dtos;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PageLinksDTOTest {

	@Test
	public void testGettersAndSetters() {

		final PageLinksDTO pageLinksDTO = new PageLinksDTO();

		pageLinksDTO.setNext("nextLink");
		pageLinksDTO.setPrev("prevLink");
		pageLinksDTO.setSelf("selfLink");

		assertEquals("nextLink", pageLinksDTO.getNext());
		assertEquals("prevLink", pageLinksDTO.getPrev());
		assertEquals("selfLink", pageLinksDTO.getSelf());
	}
}
