package com.creditsuisse.assessment.eventlogparser;


/**
 * Main class
 *
 */
public class App 
{
    public static void main(String[] args)
    {
    	String logFilePath = args[1];
    	Utils.parseEventLog(logFilePath);
    }
}
