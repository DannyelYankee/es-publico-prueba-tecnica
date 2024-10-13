package com.esPublico.app.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DateUtils {
	private final DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("d/M/yyyy");

	public String formatLocalDate(LocalDate date) {
		if (date == null) {
			log.warn("DateUtils.formatLocalDate: The provided date is null.");
			return null;
		}

		try {
			return date.format(localDateFormatter);
		} catch (final DateTimeParseException e) {
			log.error("DateUtils.formatLocalDate: Error formatting date {} to a string", date, e);
		} catch (final Exception e) {
			log.error("DateUtils.formatLocalDate: Unexpected error formatting date {}", date, e);
		}

		return null;
	}

	public LocalDate parseStringToLocalDate(String dateStr) {
		if ((dateStr == null) || dateStr.isEmpty()) {
			log.warn("DateUtils.parseStringToLocalDate: The provided date string is null or empty.");
			return null;
		}

		try {
			final var usDateFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");
			return LocalDate.parse(dateStr, usDateFormatter);
		} catch (final DateTimeParseException e1) {
			log.warn("DateUtils.parseStringToLocalDate: Error with 'M/d/yyyy' format, trying 'd/M/yyyy'", e1);
			try {
				final var localDateFormatter = DateTimeFormatter.ofPattern("d/M/yyyy");
				return LocalDate.parse(dateStr, localDateFormatter);
			} catch (final DateTimeParseException e2) {
				log.error("DateUtils.parseStringToLocalDate: Error parsing string '{}' with both formats", dateStr, e2);
			}
		}

		return null;
	}
}
