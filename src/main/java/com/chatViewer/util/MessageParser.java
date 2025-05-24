package com.chatViewer.util;

import com.chatViewer.model.ChatMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/* Parses conversation files with ".msg" suffix into ChatMessage objects. */
public class MessageParser {

    /**
     * Parses the specified file into a list of chat messages.
     *
     * @param file the path to the .msg file
     * @return list of parsed ChatMessage objects
     * @throws IOException              if file reading fails
     * @throws IllegalArgumentException if file format is invalid
     */
    public List<ChatMessage> parse(Path file) throws IOException {
        List<String> lines = Files.readAllLines(file);
        List<ChatMessage> messages = new ArrayList<>();

        int i = 0;
        while (i < lines.size()) {
            // Check for enough lines for a message (3 lines)
            if (i + 2 >= lines.size()) {
                throw new IllegalArgumentException("Incomplete message block at end of file.");
            }

            String timeLine = lines.get(i).trim();
            String nameLine = lines.get(i + 1).trim();
            String messageLine = lines.get(i + 2).trim();

            if (!timeLine.startsWith("Time:") || !nameLine.startsWith("Name:") || !messageLine.startsWith("Message:")) {
                throw new IllegalArgumentException("Invalid message format at lines " + (i + 1) + "-" + (i + 3));
            }

            String timestamp = timeLine.substring(5).trim();
            String nickname = nameLine.substring(5).trim();
            String content = messageLine.substring(8).trim();

            if (timestamp.isEmpty() || nickname.isEmpty()) {
                throw new IllegalArgumentException("Timestamp or nickname cannot be empty at lines " + (i + 1) + "-" + (i + 3));
            }

            messages.add(new ChatMessage(timestamp, nickname, content));

            // Move to next message block, skip empty line if present
            i += 3;
            if (i < lines.size() && lines.get(i).trim().isEmpty()) {
                i++;
            }
        }

        return messages;
    }
}