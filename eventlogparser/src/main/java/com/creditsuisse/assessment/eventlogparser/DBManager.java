package com.creditsuisse.assessment.eventlogparser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database Manager class
 * Containing all utility method definitions related to HSQLDB operations
 */
public class DBManager {
	/*
	 * DB connection strings
	 */
	private static String dbHostPrefix = "jdbc:hsqldb:file:";
	private static String dbClass = "org.hsqldb.jdbc.jdbcDriver";
	private static String dbSchema = "resources/db/eventlogdb";
	private static String dbUser = "sa";
	private static String dbPassword = "";
    
	/*
	 * Utility method: clean connection and relevant resource
	 */
    private static void cleanupDBConnections(ResultSet resultSet, PreparedStatement statement, Connection connection) {
    	try {
			if (resultSet != null) {
	            resultSet.close();
	        }
	        if (statement != null) {
	            statement.close();
	        }
	        if (connection != null) {
	        	connection.close();
	        }
		}
		catch (SQLException ex) {}
    }
    
    /*
	 * Utility method: SQL operation of creating the "events" table if not exist
	 */
    public static String queryWrapperCreateEventTable() {
    	String result = "";
    	Connection dbConn = null;
		PreparedStatement statement = null;
        ResultSet resultSet = null;
		String createEventTableSql = "CREATE TABLE IF NOT EXISTS events (id VARCHAR(255), duration DECIMAL, type VARCHAR(10), host VARCHAR(255), alert BOOLEAN, PRIMARY KEY (id))";
		
		try {
			dbConn = DriverManager.getConnection(dbHostPrefix + dbSchema, dbUser, dbPassword);
			if (dbConn != null) {
				statement = dbConn.prepareStatement(createEventTableSql);
	            statement.execute();
	            result = "success";
			}
		}
		catch (SQLException ex) {
			result = ex.getMessage();
		}
		finally {
			cleanupDBConnections(resultSet, statement, dbConn);
		}
		return result;
    }
    
    /*
	 * Utility method: SQL query for event entries with specified id and in "STARTED" state
	 */
    public static long queryWrapperSelectEventStartEntryById(String id) {
    	long duration = -1;
    	Connection dbConn = null;
		PreparedStatement statement = null;
        ResultSet resultSet = null;
        String selectEventDurationByIdSql = "SELECT duration FROM events WHERE id = ? AND state = 'STARTED'";
        
        try {
			dbConn = DriverManager.getConnection(dbHostPrefix + dbSchema, dbUser, dbPassword);
			if (dbConn != null) {
				statement = dbConn.prepareStatement(selectEventDurationByIdSql);
				statement.setString(0, id);
            	
            	resultSet = statement.executeQuery();
            	if (resultSet.next()) {
            		duration = resultSet.getLong("duration");
            	}
			}
		}
		catch (SQLException ex) {
			ex.printStackTrace();
			duration = -1;
		}
		finally {
			cleanupDBConnections(resultSet, statement, dbConn);
		}
    	
    	return duration;
    }
    
    /*
	 * Utility method: SQL query for event entries with specified id and in "FINISHED" state
	 */
    public static long queryWrapperSelectEventEndEntryById(String id) {
    	long duration = -1;
    	Connection dbConn = null;
		PreparedStatement statement = null;
        ResultSet resultSet = null;
        String selectEventDurationByIdSql = "SELECT duration FROM events WHERE id = ? AND state = 'FINISHED'";
        
        try {
			dbConn = DriverManager.getConnection(dbHostPrefix + dbSchema, dbUser, dbPassword);
			if (dbConn != null) {
				statement = dbConn.prepareStatement(selectEventDurationByIdSql);
				statement.setString(0, id);
            	
            	resultSet = statement.executeQuery();
            	if (resultSet.next()) {
            		duration = resultSet.getLong("duration");
            	}
			}
		}
		catch (SQLException ex) {
			ex.printStackTrace();
			duration = -1;
		}
		finally {
			cleanupDBConnections(resultSet, statement, dbConn);
		}
    	
    	return duration;
    }
    
    /*
	 * Utility method: SQL operation of inserting new event entry
	 */
    public static String queryWrapperInsertEvent(String id, long period, String type, String host, boolean alert) {
    	Connection dbConn = null;
		PreparedStatement statement = null;
        ResultSet resultSet = null;
        String insertEventSql = "INSERT INTO events VALUES ?, ?, ?, ?, ?";
        String result = "";
		
		try {
			dbConn = DriverManager.getConnection(dbHostPrefix + dbSchema, dbUser, dbPassword);
			if (dbConn != null) {
				statement = dbConn.prepareStatement(insertEventSql);
				statement.setString(0, id);
            	statement.setLong(1, period);
            	statement.setString(2, type);
            	statement.setString(3, host);
            	statement.setBoolean(4, alert);
            	
            	statement.execute();
            	result = "success";
			}
		}
		catch (SQLException ex) {
			result = ex.getMessage();
		}
		finally {
			cleanupDBConnections(resultSet, statement, dbConn);
		}
		return result;
    }
    
    /*
	 * Utility method: SQL operation of updating duration and alert (boolean) fields of event entry with specified id
	 */
    public static int queryWrapperUpdateEvent(String id, long duration) {
    	int updateStatus = 0;
    	Connection dbConn = null;
		PreparedStatement statement = null;
        ResultSet resultSet = null;
        String updateEventPeriodSql = "UPDATE events SET duration = ? AND alert = ? WHERE id = ?";
		
		try {
			dbConn = DriverManager.getConnection(dbHostPrefix + dbSchema, dbUser, dbPassword);
			if (dbConn != null) {
				statement = dbConn.prepareStatement(updateEventPeriodSql);
				statement.setLong(0, duration);
				statement.setBoolean(1, (duration >= 4));
            	statement.setString(2, id);
            	
            	updateStatus = statement.executeUpdate();
			}
		}
		catch (SQLException ex) {
			updateStatus = -1;
		}
		finally {
			cleanupDBConnections(resultSet, statement, dbConn);
		}
    	
    	return updateStatus;
    }
}
