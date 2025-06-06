package com.example.chatviewer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

/**
 * JUnit tests for Message parsing and validation.
 */
public class MessageTest {

    @Test
    public void testValidMessage() throws Exception {
        Message m = new Message("2023-05-01T12:34:56", "Alice", "Hello");
        assertEquals(LocalDateTime.of(2023, 5, 1, 12, 34, 56), m.getTimestamp());
        assertEquals("Alice", m.getNickname());
        assertEquals("Hello", m.getContent());
    }

    @Test
    public void testInvalidTimestamp() {
        assertThrows(TimestampFormatException.class, () -> new Message("not-a-timestamp", "Bob", "Test"));
    }

    @Test
    public void testEmptyContent() {
        assertThrows(IllegalArgumentException.class, () -> new Message("2023-05-01T00:00:00", "Bob", ""));
    }

    @Test
    public void testInvalidNickname() throws Exception {
        Message m = new Message("2023-05-01T08:00:00", "Bad Nick!", "Hi");
        assertEquals("...", m.getNickname());
    }
}