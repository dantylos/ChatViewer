package com.chatViewer.model;

/**
 * Represents a chat message with timestamp, nickname and content.
 *
 * @param timestamp the message creation time (any non-empty string)
 * @param nickname  the user nickname who sent the message
 * @param content   the textual content of the message (may include emoticons)
 */
public record ChatMessage(String timestamp, String nickname, String content) {}