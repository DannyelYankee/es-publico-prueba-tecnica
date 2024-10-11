package com.esPublico.app.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class DateUtilsTest {

	@InjectMocks
	private DateUtils dateUtils;

	private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testFormatLocalDate_NullDate() {
		final String result = dateUtils.formatLocalDate(null);
		assertNull(result);
	}

	@Test
	public void testFormatLocalDate_ValidDate() {
		final LocalDate date = LocalDate.of(2024, 10, 11);
		final String result = dateUtils.formatLocalDate(date);
		assertEquals("11/10/2024", result);
	}

	@Test
	public void testFormatLocalDate_InvalidDateFormat() {
		final DateUtils spyDateUtils = spy(dateUtils);
		final LocalDate invalidDate = mock(LocalDate.class);

		doThrow(new DateTimeParseException("Test Exception", "InvalidDate", 0)).when(invalidDate).format(any());

		final String result = spyDateUtils.formatLocalDate(invalidDate);
		assertNull(result);
		verify(spyDateUtils).formatLocalDate(invalidDate);
	}

	@Test
	public void testFormatLocalDate_UnexpectedException() {
		final DateUtils spyDateUtils = spy(dateUtils);
		final LocalDate invalidDate = mock(LocalDate.class);

		doThrow(new RuntimeException("Unexpected exception")).when(invalidDate).format(any());

		final String result = spyDateUtils.formatLocalDate(invalidDate);
		assertNull(result);
		verify(spyDateUtils).formatLocalDate(invalidDate);
	}

	@Test
	public void testParseStringToLocalDate_NullOrEmptyString() {

		final LocalDate resultNull = dateUtils.parseStringToLocalDate(null);
		assertNull(resultNull);

		final LocalDate resultEmpty = dateUtils.parseStringToLocalDate("");
		assertNull(resultEmpty);
	}

	@Test
	public void testParseStringToLocalDate_ValidDateFormats() {
		// Formato MM/dd/yyyy (Formato de EE.UU.)
		final String usDateStr = "10/11/2024";
		final LocalDate resultUsFormat = dateUtils.parseStringToLocalDate(usDateStr);
		assertNotNull(resultUsFormat);
		assertEquals(LocalDate.of(2024, 10, 11), resultUsFormat);

		// Formato dd/MM/yyyy (Formato local)
		final String localDateStr = "11/10/2024";
		final LocalDate resultLocalFormat = dateUtils.parseStringToLocalDate(localDateStr);
		assertNotNull(resultLocalFormat);
		assertEquals(LocalDate.of(2024, 11, 10), resultLocalFormat);
	}

	@Test
	public void testParseStringToLocalDate_InvalidUsFormat() {
		final String invalidUsDateStr = "invalid_date";
		final LocalDate result = dateUtils.parseStringToLocalDate(invalidUsDateStr);

		assertNull(result);
	}

	@Test
	public void testParseStringToLocalDate_InvalidLocalFormat() {

		final String invalidLocalDateStr = "31/31/2024";
		final LocalDate result = dateUtils.parseStringToLocalDate(invalidLocalDateStr);

		assertNull(result);
	}

	@Test
	public void testParseStringToLocalDate_UsFormatFails_LocalFormatSucceeds() {

		final String dateStr = "31/01/2024";

		final LocalDate result = dateUtils.parseStringToLocalDate(dateStr);

		assertNotNull(result);
		assertEquals(LocalDate.of(2024, 1, 31), result);

	}

}
