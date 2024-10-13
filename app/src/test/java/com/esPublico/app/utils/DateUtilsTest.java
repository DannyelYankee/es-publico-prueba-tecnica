package com.esPublico.app.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class DateUtilsTest {

	@InjectMocks
	private DateUtils dateUtils;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testFormatLocalDate_NullDate() {

		final DateUtils spyDateUtils = spy(dateUtils);

		final String result = spyDateUtils.formatLocalDate(null);

		assertNull(result);

	}

	@Test
	public void testFormatLocalDate_ValidDate() {

		final LocalDate date = LocalDate.of(2023, 10, 13);
		final String expectedFormattedDate = "13/10/2023";

		final String result = dateUtils.formatLocalDate(date);

		assertEquals(expectedFormattedDate, result);
	}

	@Test(expected = DateTimeParseException.class)
	public void testFormatLocalDate_DateTimeParseException() {

		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
		final LocalDate invalidDate = LocalDate.now();
		final DateUtils dateUtilsSpy = spy(dateUtils);

		doThrow(new DateTimeParseException("Invalid date", "text", 0)).when(dateUtilsSpy).formatLocalDate(invalidDate);

		final String result = dateUtilsSpy.formatLocalDate(invalidDate);

		assertNull(result);
	}

	@Test(expected = Exception.class)
	public void testFormatLocalDate_GeneralException() {
		// Given
		final LocalDate validDate = LocalDate.now();
		final DateUtils dateUtilsSpy = spy(dateUtils);

		doThrow(new Exception("Unexpected error")).when(dateUtilsSpy).formatLocalDate(validDate);

		final String result = dateUtilsSpy.formatLocalDate(validDate);

		// Then
		assertNull(result);
	}

	@Test
	public void testParseStringToLocalDate_ValidUsFormat() {

		final String dateStr = "10/13/2023";
		final LocalDate expectedDate = LocalDate.of(2023, 10, 13);

		final LocalDate result = dateUtils.parseStringToLocalDate(dateStr);

		assertEquals(expectedDate, result);
	}

	@Test
	public void testParseStringToLocalDate_ValidLocalFormat() {

		final String dateStr = "13/10/2023";
		final LocalDate expectedDate = LocalDate.of(2023, 10, 13);

		final LocalDate result = dateUtils.parseStringToLocalDate(dateStr);

		assertEquals(expectedDate, result);
	}

	@Test
	public void testParseStringToLocalDate_InvalidDate() {

		final String dateStr = "invalid-date";

		final DateUtils spyDateUtils = spy(dateUtils);

		final LocalDate result = spyDateUtils.parseStringToLocalDate(dateStr);

		assertNull(result);

	}

	@Test
	public void testParseStringToLocalDate_NullOrEmptyString() {

		final DateUtils spyDateUtils = spy(dateUtils);

		final LocalDate resultNull = spyDateUtils.parseStringToLocalDate(null);
		final LocalDate resultEmpty = spyDateUtils.parseStringToLocalDate("");

		assertNull(resultNull);
		assertNull(resultEmpty);
	}
}
