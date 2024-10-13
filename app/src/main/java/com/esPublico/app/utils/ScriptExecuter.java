package com.esPublico.app.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class ScriptExecuter {

	private final JdbcTemplate jdbcTemplate;

	public void executeScript(String scriptPath) throws Exception {

		final var inputStream = getInputStream(scriptPath);

		final String script = getScript(inputStream, scriptPath);

		jdbcTemplate.execute(script);
		log.info("Executed script: {}", scriptPath);
	}

	public String getScript(InputStream inputStream, String scriptPath) throws IOException {

		final var script = FileCopyUtils.copyToString(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
		if ((script == null) || script.trim().isEmpty()) {
			throw new IllegalArgumentException("Script is empty: " + scriptPath);
		}
		return script;

	}

	public InputStream getInputStream(String scriptPath) {
		final var inputStream = getClass().getResourceAsStream("/scripts/" + scriptPath);

		if (inputStream == null) {
			throw new IllegalArgumentException("Script not found: " + scriptPath);
		}
		return inputStream;
	}
}
