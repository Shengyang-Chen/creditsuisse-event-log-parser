package com.creditsuisse.assessment.eventlogparser;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UtilsTest {
	
	public static final String inputPath = "resources/logfile.txt";
	public static final String outputPath = "resources/db/eventlogdb.*";

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testParseEventLog() {
		Utils.parseEventLog(inputPath);
		assertTrue(Files.exists(Paths.get("resources/db/eventlogdb")), "Should generate database file output.");
	}

}
