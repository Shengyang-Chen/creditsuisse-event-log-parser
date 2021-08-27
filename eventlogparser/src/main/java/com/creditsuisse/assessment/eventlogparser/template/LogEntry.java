package com.creditsuisse.assessment.eventlogparser.template;

import com.google.gson.annotations.SerializedName;

public final class LogEntry {
	@SerializedName("id")
	final String id;
	
	@SerializedName("state")
	final String state;
	
	@SerializedName("type")
	final String type;
	
	@SerializedName("host")
	final String host;
	
	@SerializedName("timestamp")
	final long timestamp;
	
	private LogEntry(final String id, final String state, final String type, final String host, final long timestamp) {
		this.id = id;
		this.state = state;
		this.type = type;
		this.host = host;
		this.timestamp = timestamp;
	}
	
	static LogEntry of(final String id, final String state, final String type, final String host, final long timestamp) {
		return new LogEntry(id, state, type, host, timestamp);
	}
	
	public String getLogEntryId() {
		return this.id;
	}
	
	public String getLogEntryState() {
		return this.state;
	}
	
	public String getLogEntryType() {
		return this.type;
	}
	
	public String getLogEntryHost() {
		return this.host;
	}
	
	public long getLogEntryTimestamp() {
		return this.timestamp;
	}
	
	@Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LogEntry entry = (LogEntry)o;
        return id == entry.id;
    }
}
