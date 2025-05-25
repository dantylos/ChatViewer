package com.chatViewer;

import com.chatViewer.model.ChatMessage;
import com.chatViewer.util.MessageParser;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/* Unit tests for MessageParser. */
class MessageParserTest {

    @Test
    void testParseValidFile() throws IOException {
        String content = """
            Time:2025-05-25 12:00
            Name:Alice
            Message:Hello :)
            
            Time:2025-05-25 12:01
            Name:Bob
            Message:Hi Alice :(
            """;
        Path tempFile = Files.createTempFile("test", ".msg");
        Files.writeString(tempFile, content);

        MessageParser parser = new MessageParser();
        List<ChatMessage> messages = parser.parse(tempFile);

        assertEquals(2, messages.size());
        assertEquals("Alice", messages.get(0).nickname());
        assertEquals("Hi Alice :(", messages.get(1).content());

        Files.delete(tempFile);
    }

    @Test
    void testParseInvalidFileThrows() throws IOException {
        String content = """
            Time:2025-05-25 12:00
            Name:
            Message:Hello
            """;
        Path tempFile = Files.createTempFile("test", ".msg");
        Files.writeString(tempFile, content);

        MessageParser parser = new MessageParser();
        assertThrows(IllegalArgumentException.class, () -> parser.parse(tempFile));

        Files.delete(tempFile);
    }
}
