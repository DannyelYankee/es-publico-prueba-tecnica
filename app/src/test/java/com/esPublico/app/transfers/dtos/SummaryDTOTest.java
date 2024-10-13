package com.esPublico.app.transfers.dtos;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SummaryDTOTest {

	@Test
	public void testConstructorAndGetters() {

		final String value = "value";
		final Long total = 100L;

		final SummaryDTO summaryDTO = new SummaryDTO(value, total);

		assertEquals("summaryValue", summaryDTO.getValue());
		assertEquals(Long.valueOf(100L), summaryDTO.getTotal());
	}

	@Test
	public void testSetters() {

		final SummaryDTO summaryDTO = new SummaryDTO("initialValue", 50L);

		summaryDTO.setValue("newValue");
		summaryDTO.setTotal(200L);

		assertEquals("newValue", summaryDTO.getValue());
		assertEquals(Long.valueOf(200L), summaryDTO.getTotal());
	}

}
