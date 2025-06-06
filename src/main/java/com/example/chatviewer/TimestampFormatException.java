package com.example.chatviewer;

/**
 * Exception thrown when a timestamp string is not in valid ISO 8601 format.
 */
public class TimestampFormatException extends Exception {
    /**
     * Constructs the exception with the invalid timestamp string.
     * @param timestampStr the invalid timestamp
     */
    public TimestampFormatException(String timestampStr) {
        super("Invalid timestamp: " + timestampStr);
    }
}