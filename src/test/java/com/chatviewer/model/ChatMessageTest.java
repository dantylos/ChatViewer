package com.chatviewer.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ChatMessage record.
 */
class ChatMessageTest {

    @Test
    void testRecordFields() {
        ChatMessage msg = new ChatMessage("2025-05-31T12:00:00", "Alice", "Hello :)");
        assertEquals("2025-05-31T12:00:00", msg.timestamp());
        assertEquals("Alice", msg.nickname());
        assertEquals("Hello :)", msg.content());
    }

    @Test
    void testNullFieldsThrows() {
        assertThrows(IllegalArgumentException.class, () -> new ChatMessage(null, "Bob", "Hi"));
        assertThrows(IllegalArgumentException.class, () -> new ChatMessage("2025-05-31T12:00:00", null, "Hi"));
        assertThrows(IllegalArgumentException.class, () -> new ChatMessage("2025-05-31T12:00:00", "Bob", null));
    }
}
