package com.esPublico.app.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public class ScriptExecuterTest {

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private Logger log;

	@InjectMocks
	private ScriptExecuter scriptExecuter;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testExecuteScript_success() throws Exception {

		final String scriptPath = "test_script.sql";
		final String scriptContent = "CREATE TABLE test (id INT);";
		final InputStream inputStream = new ByteArrayInputStream(scriptContent.getBytes(StandardCharsets.UTF_8));

		final ScriptExecuter spyExecuter = spy(scriptExecuter);
		doReturn(inputStream).when(spyExecuter).getInputStream(scriptPath);
		doReturn(scriptContent).when(spyExecuter).getScript(inputStream, scriptPath);

		spyExecuter.executeScript(scriptPath);

		verify(jdbcTemplate, times(1)).execute(scriptContent);

	}

	@Test
	void testExecuteScript_emptyScript_throwsException() throws Exception {
		// Arrange
		final String scriptPath = "test_script.sql";
		final InputStream inputStream = new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8));

		final ScriptExecuter spyExecuter = spy(scriptExecuter);
		doReturn(inputStream).when(spyExecuter).getInputStream(scriptPath);

		final Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			spyExecuter.executeScript(scriptPath);
		});

		assertEquals("Script is empty: " + scriptPath, exception.getMessage());
	}

	@Test
	void testExecuteScript_scriptNotFound_throwsException() {

		final String scriptPath = "test.sql";

		final ScriptExecuter spyExecuter = spy(scriptExecuter);

		doThrow(new IllegalArgumentException("Script not found: " + scriptPath)).when(spyExecuter)
				.getInputStream(scriptPath);

		final Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			spyExecuter.executeScript(scriptPath);
		});

		assertEquals("Script not found: " + scriptPath, exception.getMessage());
	}

	@Test
	void testGetScript_success() throws IOException {

		final String scriptContent = "CREATE TABLE test (id INT);";
		final InputStream inputStream = new ByteArrayInputStream(scriptContent.getBytes(StandardCharsets.UTF_8));

		final String result = scriptExecuter.getScript(inputStream, "test_script.sql");

		assertEquals(scriptContent, result);
	}

	@Test
	void testGetScript_emptyInputStream_throwsException() throws IOException {

		final InputStream inputStream = new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8));

		final Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			scriptExecuter.getScript(inputStream, "test_script.sql");
		});

		assertEquals("Script is empty: test_script.sql", exception.getMessage());
	}

	@Test
	void testGetInputStream_scriptFound() {
		// Arrange
		final String scriptPath = "test_script.sql";
		final InputStream mockInputStream = new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8));

		final ScriptExecuter spyExecuter = spy(scriptExecuter);
		doReturn(mockInputStream).when(spyExecuter).getInputStream(scriptPath);

		final InputStream result = spyExecuter.getInputStream(scriptPath);

		assertNotNull(result);
	}

	@Test
	void testGetInputStream_scriptNotFound_throwsException() {

		final String scriptPath = "test_script.sql";

		final Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			scriptExecuter.getInputStream(scriptPath);
		});

		assertEquals("Script not found: " + scriptPath, exception.getMessage());
	}
}
