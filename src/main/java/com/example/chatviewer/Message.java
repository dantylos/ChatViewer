package com.example.chatviewer;

/**
 * Represents a single chat message with timestamp, nickname, and content.
 */
public class Message {

    private final String timestamp;
    private final String nickname;
    private final String content;

    /**
     * Constructs a Message from raw string lines according to .msg format.
     * @param timestampLine line starting with "Time:"
     * @param nicknameLine line starting with "Name:"
     * @param contentLine line starting with "Message:"
     * @throws IllegalArgumentException if format is invalid
     */
    public Message(String timestampLine, String nicknameLine, String contentLine) {
        // Parse timestamp (accept any non-empty string after "Time:")
        if (!timestampLine.startsWith("Time:")) {
            throw new IllegalArgumentException("Invalid timestamp line format: " + timestampLine);
        }
        String tsValue = timestampLine.substring(5).trim();
        if (tsValue.isEmpty()) {
            throw new IllegalArgumentException("Timestamp cannot be empty");
        }
        this.timestamp = tsValue;

        // Parse nickname (accept any non-empty string after "Name:")
        if (!nicknameLine.startsWith("Name:")) {
            throw new IllegalArgumentException("Invalid nickname line format: " + nicknameLine);
        }
        String nickValue = nicknameLine.substring(5).trim();
        if (nickValue.isEmpty()) {
            throw new IllegalArgumentException("Nickname cannot be empty");
        }
        this.nickname = nickValue;

        // Parse content (accept any string after "Message:", including empty)
        if (!contentLine.startsWith("Message:")) {
            throw new IllegalArgumentException("Invalid content line format: " + contentLine);
        }
        this.content = contentLine.substring(8); // Don't trim - preserve spaces
    }

    /**
     * Alternative constructor for direct values (for testing)
     */
    public Message(String timestamp, String nickname, String content, boolean direct) {
        if (direct) {
            this.timestamp = timestamp;
            this.nickname = nickname;
            this.content = content;
        } else {
            throw new IllegalArgumentException("Use primary constructor for .msg format");
        }
    }

    /** @return the message timestamp as string */
    public String getTimestamp() {
        return timestamp;
    }

    /** @return the nickname */
    public String getNickname() {
        return nickname;
    }

    /** @return the message content */
    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: %s", timestamp, nickname, content);
    }
}