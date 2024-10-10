package com.esPublico.app.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateUtils {
	private static final DateTimeFormatter US_DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy");
	private static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy");

	private DateUtils() {
		throw new UnsupportedOperationException("DateUtils is a utility class and cannot be instantiated");
	}

	public static String formatLocalDate(LocalDate date) {
		if (date == null) {
			log.warn("DateUtils.formatLocalDate: The provided date is null.");
			return null;
		}

		try {
			return date.format(LOCAL_DATE_FORMATTER);
		} catch (final DateTimeParseException e) {
			log.error("DateUtils.formatLocalDate: Error formatting date {} to a string", date, e);
		} catch (final Exception e) {
			log.error("DateUtils.formatLocalDate: Unexpected error formatting date {}", date, e);
		}

		return null;
	}

	public static LocalDate parseStringToLocalDate(String dateStr) {
		if ((dateStr == null) || dateStr.isEmpty()) {
			log.warn("DateUtils.parseStringToLocalDate: The provided date string is null or empty.");
			return null;
		}

		try {

			return LocalDate.parse(dateStr, US_DATE_FORMATTER);
		} catch (final DateTimeParseException e1) {
			log.warn("DateUtils.parseStringToLocalDate: Error with 'dd/MM/yyyy' format, trying 'MM/dd/yyyy'", e1);
			try {
				return LocalDate.parse(dateStr, LOCAL_DATE_FORMATTER);
			} catch (final DateTimeParseException e2) {
				log.error("DateUtils.parseStringToLocalDate: Error parsing string '{}' with both formats", dateStr, e2);
			}
		} catch (final Exception e) {
			log.error("DateUtils.parseStringToLocalDate: Unexpected error parsing string '{}' to LocalDate", dateStr,
					e);
		}

		return null;
	}

}
