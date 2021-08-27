package com.creditsuisse.assessment.eventlogparser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.google.gson.Gson;

import com.creditsuisse.assessment.eventlogparser.template.*;

public class Utils {
	public static void parseEventLog(String path) {
		try {
			Path inputPath = Paths.get(path).toAbsolutePath();
			Gson eventLogGsonObj = new Gson();
			DBManager.queryWrapperCreateEventTable();  // Create table if not exist
			
			// Read input log file line by line, using parallel stream for time optimization and avoiding loading entire file to memory
			try (Stream<String> lineJsonStream = Files.lines(inputPath)) {
				lineJsonStream.parallel().forEach(line -> {
					LogEntry entry = eventLogGsonObj.fromJson(line, LogEntry.class);
					if (entry != null) {
						// Retrieve attribute values based on keys in each Json object
						String logEntryId = entry.getLogEntryId(); 
		            	String logEntryState = entry.getLogEntryState(); 
		            	String logEntryType = entry.getLogEntryType();
		            	String logEntryHost = entry.getLogEntryHost();
		            	long logEntryTimestamp = entry.getLogEntryTimestamp();
		            	long eventStartTime = -1;
		            	long eventEndTime = -1;
		            	long logEntryDuration = Long.MAX_VALUE;
		            	
		            	switch(logEntryState) {
		            		case "STARTED": // For event-start entries, update duration if the associated "end" entry already inserted, otherwise create the new event entry and record start time as duration
		            			eventEndTime = DBManager.queryWrapperSelectEventEndEntryById(logEntryId);
		            			if (eventEndTime != -1) {
		            				logEntryDuration = Math.abs(logEntryTimestamp - eventEndTime);
		            				int status = DBManager.queryWrapperUpdateEvent(logEntryId, logEntryDuration);
		            			}
		            			else {
		            				DBManager.queryWrapperInsertEvent(logEntryId, logEntryTimestamp, logEntryType, logEntryHost, (logEntryDuration >= 4));
		            			}
		            			break;
		            		case "FINISHED": // For event-end entries, update duration if the associated "start" entry already inserted, otherwise create the new event entry and record end time as duration
		            			eventStartTime = DBManager.queryWrapperSelectEventStartEntryById(logEntryId);
		            			if (eventStartTime != -1) {
		            				logEntryDuration = Math.abs(logEntryTimestamp - eventStartTime);
		            				int status = DBManager.queryWrapperUpdateEvent(logEntryId, logEntryDuration);
		            			}
		            			else {
		            				DBManager.queryWrapperInsertEvent(logEntryId, logEntryTimestamp, logEntryType, logEntryHost, (logEntryDuration >= 4));
		            			}
		            			break;
		            		default:
		            			break;
		            	}
					}
				});
			}
		}
		catch (IOException ex) {}
	}
	
	
}
