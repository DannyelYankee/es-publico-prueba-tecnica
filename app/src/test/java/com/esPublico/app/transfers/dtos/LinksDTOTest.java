package com.esPublico.app.transfers.dtos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LinksDTOTest {

	private LinksDTO linksDTO;

	@Before
	public void setUp() {
		linksDTO = new LinksDTO();
	}

	@Test
	public void testGettersAndSetters() {

		final String selfLink = "link";

		linksDTO.setSelf(selfLink);

		assertEquals(selfLink, linksDTO.getSelf());
	}

	@Test
	public void testJsonSerialization() throws Exception {

		final String selfLink = "link";
		linksDTO.setSelf(selfLink);

		final ObjectMapper objectMapper = new ObjectMapper();

		final String jsonString = objectMapper.writeValueAsString(linksDTO);

		assertNotNull(jsonString);
		assertEquals("{\"self\":\"link\"}", jsonString);
	}

	@Test
	public void testJsonDeserialization() throws Exception {
		final String jsonString = "{\"self\":\"link\"}";

		final ObjectMapper objectMapper = new ObjectMapper();

		final LinksDTO deserializedDTO = objectMapper.readValue(jsonString, LinksDTO.class);

		assertNotNull(deserializedDTO);
		assertEquals("link", deserializedDTO.getSelf());
	}
}
