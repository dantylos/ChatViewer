package com.chatViewer;

import com.chatViewer.model.ChatMessage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/* Unit tests for ChatMessage record. */
class ChatMessageTest {

    @Test
    void testRecordFields() {
        ChatMessage msg = new ChatMessage("2025-05-25 12:00", "Alice", "Hello :)");
        assertEquals("2025-05-25 12:00", msg.timestamp());
        assertEquals("Alice", msg.nickname());
        assertEquals("Hello :)", msg.content());
    }
}
