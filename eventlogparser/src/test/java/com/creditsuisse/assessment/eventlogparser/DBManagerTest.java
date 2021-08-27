package com.creditsuisse.assessment.eventlogparser;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DBManagerTest {

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
	void testQueryWrapperCreateEventTable() {
		String result = DBManager.queryWrapperCreateEventTable();
		assertEquals(result, "success");
	}

	@Test
	void testQueryWrapperSelectEventStartEntryById() {
		long result = DBManager.queryWrapperSelectEventStartEntryById("scsmbstgra");
		assertNotEquals(result, -1);
	}

	@Test
	void testQueryWrapperSelectEventEndEntryById() {
		long result = DBManager.queryWrapperSelectEventEndEntryById("scsmbstgra");
		assertNotEquals(result, -1);
	}

	@Test
	void testQueryWrapperInsertEvent() {
		String result = DBManager.queryWrapperInsertEvent("scsmbstgra", 1, null, null, false);
		assertEquals(result, "success");
	}

	@Test
	void testQueryWrapperUpdateEvent() {
		int result = DBManager.queryWrapperUpdateEvent("scsmbstgra", 2);
		assertEquals(result, 1);
	}

}
