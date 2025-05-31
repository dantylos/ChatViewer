package com.chatviewer.util;

import com.chatviewer.model.ChatMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the MessageParser with validation scenarios.
 */
class MessageParserTest {

    @TempDir
    Path tempDir;

    @Test
    void testParseValidFile() throws IOException, ValidationException {
        String content = """
            Time:2025-05-31T14:30:00
            Name:Alice_123
            Message:Hello everyone :) How is your day?
            
            Time:2025-05-31T14:31:15
            Name:Bob-test
            Message:Great! Working on assignment :(
            """;
        Path tempFile = tempDir.resolve("valid.msg");
        Files.writeString(tempFile, content, StandardCharsets.UTF_8);

        MessageParser parser = new MessageParser();
        List<ChatMessage> messages = parser.parse(tempFile);

        assertEquals(2, messages.size());
        assertEquals("Alice_123", messages.get(0).nickname());
        assertEquals("Great! Working on assignment :(", messages.get(1).content());
    }

    @Test
    void testParseInvalidTimestampFormat() throws IOException {
        String content = """
            Time:not-a-timestamp
            Name:Alice
            Message:Hello
            """;
        Path tempFile = tempDir.resolve("invalid_timestamp.msg");
        Files.writeString(tempFile, content, StandardCharsets.UTF_8);

        MessageParser parser = new MessageParser();
        ValidationException exception = assertThrows(ValidationException.class, () -> parser.parse(tempFile));
        assertTrue(exception.getMessage().contains("Malformed timestamp format"));
    }

    @Test
    void testParseInvalidNicknameCharacters() throws IOException {
        String content = """
            Time:2025-05-31T14:30:00
            Name:Alice@#$
            Message:Hello
            """;
        Path tempFile = tempDir.resolve("invalid_nickname.msg");
        Files.writeString(tempFile, content, StandardCharsets.UTF_8);

        MessageParser parser = new MessageParser();
        ValidationException exception = assertThrows(ValidationException.class, () -> parser.parse(tempFile));
        assertTrue(exception.getMessage().contains("invalid characters"));
    }

    @Test
    void testParseWhitespaceOnlyMessage() throws IOException {
        String content = """
            Time:2025-05-31T14:30:00
            Name:Alice
            Message:    \t
            """;
        Path tempFile = tempDir.resolve("whitespace_message.msg");
        Files.writeString(tempFile, content, StandardCharsets.UTF_8);

        MessageParser parser = new MessageParser();
        ValidationException exception = assertThrows(ValidationException.class, () -> parser.parse(tempFile));
        assertTrue(exception.getMessage().contains("cannot contain only whitespace"));
    }

    @Test
    void testParseEmptyMessageAllowed() throws IOException, ValidationException {
        String content = """
            Time:2025-05-31T14:30:00
            Name:Alice
            Message:
            """;
        Path tempFile = tempDir.resolve("empty_message.msg");
        Files.writeString(tempFile, content, StandardCharsets.UTF_8);

        MessageParser parser = new MessageParser();
        List<ChatMessage> messages = parser.parse(tempFile);

        assertEquals(1, messages.size());
        assertEquals("", messages.getFirst().content());
    }

    @Test
    void testParseMaxLengthExceeded() throws IOException {
        String longNickname = "A".repeat(1001); // Exceeds 1000 characters limit
        String content = String.format("""
            Time:2025-05-31T14:30:00
            Name:%s
            Message:Hello
            """, longNickname);
        Path tempFile = tempDir.resolve("long_nickname.msg");
        Files.writeString(tempFile, content, StandardCharsets.UTF_8);

        MessageParser parser = new MessageParser();
        ValidationException exception = assertThrows(ValidationException.class, () -> parser.parse(tempFile));
        assertTrue(exception.getMessage().contains("exceeds maximum length"));
    }
}
