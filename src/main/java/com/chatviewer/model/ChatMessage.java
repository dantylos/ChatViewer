package com.chatviewer.model;

/**
 * Immutable record representing a single chat message.
 * Contains timestamp, nickname, and message content with validation constraints.
 *
 * @param timestamp ISO 8601 formatted timestamp (max 1000 characters)
 * @param nickname user display name with restricted character set (max 1000 characters)
 * @param content message body with UTF-8 encoding (max 1000 characters)
 *
 * @author Chat Viewer Team
 * @version 1.0.0
 * @since 2025-05-31
 */
public record ChatMessage(String timestamp, String nickname, String content) {

    /**
     * Compact constructor with basic validation.
     * Ensures non-null values for all fields.
     *
     * @throws IllegalArgumentException if any field is null
     */
    public ChatMessage {
        if (timestamp == null || nickname == null || content == null) {
            throw new IllegalArgumentException("ChatMessage fields cannot be null");
        }
    }
}