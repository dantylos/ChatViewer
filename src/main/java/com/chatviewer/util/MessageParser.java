package com.chatviewer.util;

import com.chatviewer.model.ChatMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Enhanced message parser with comprehensive validation according to specifications.
 * Handles UTF-8 encoding, field length limits, and strict format validation.
 *
 * @author Chat Viewer Team
 * @version 1.0.0
 * @since 2025-05-31
 */
public class MessageParser {

    private static final Logger LOGGER = Logger.getLogger(MessageParser.class.getName());

    /** Maximum allowed length for any field */
    private static final int MAX_FIELD_LENGTH = 1000;

    /** Pattern for valid nicknames: alphanumeric, underscore, hyphen, period */
    private static final Pattern VALID_NICKNAME_PATTERN = Pattern.compile("^[A-Za-z0-9._-]+$");

    /** Pattern for whitespace-only content detection */
    private static final Pattern WHITESPACE_ONLY_PATTERN = Pattern.compile("^\\s*$");

    /** ISO 8601 date formatter for timestamp validation */
    private static final DateTimeFormatter ISO_8601_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Parses the specified file into a list of validated chat messages.
     *
     * @param file the path to the .msg file (must be UTF-8 encoded)
     * @return list of validated ChatMessage objects
     * @throws IOException if file reading fails
     * @throws ValidationException if validation rules are violated
     */
    public List<ChatMessage> parse(Path file) throws IOException, ValidationException {
        LOGGER.info("Starting to parse file: " + file.toString());

        // Read file with UTF-8 encoding as specified
        List<String> lines;
        try {
            lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            LOGGER.fine("Successfully read " + lines.size() + " lines from file");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to read file with UTF-8 encoding", e);
            throw new IOException("Failed to read file with UTF-8 encoding: " + e.getMessage(), e);
        }

        List<ChatMessage> messages = new ArrayList<>();
        int lineNumber = 1;

        int i = 0;
        while (i < lines.size()) {
            try {
                ChatMessage message = parseMessageBlock(lines, i, lineNumber);
                messages.add(message);
                LOGGER.fine("Successfully parsed message from line " + lineNumber);

                // Move to next message block (3 lines + optional empty line)
                i += 3;
                if (i < lines.size() && lines.get(i).trim().isEmpty()) {
                    i++;
                }
                lineNumber = i + 1;

            } catch (ValidationException e) {
                LOGGER.log(Level.WARNING, "Validation failed at line " + lineNumber, e);
                throw new ValidationException("Validation failed at line " + lineNumber + ": " + e.getMessage(), e);
            }
        }

        if (messages.isEmpty()) {
            throw new ValidationException("No valid messages found in file");
        }

        LOGGER.info("Successfully parsed " + messages.size() + " messages");
        return messages;
    }

    /**
     * Parses a single message block (3 lines) with strict validation.
     *
     * @param lines all file lines
     * @param startIndex starting index for this message block
     * @param lineNumber current line number for error reporting
     * @return validated ChatMessage object
     * @throws ValidationException if validation fails
     */
    private ChatMessage parseMessageBlock(List<String> lines, int startIndex, int lineNumber)
            throws ValidationException {

        if (startIndex + 2 >= lines.size()) {
            throw new ValidationException("Incomplete message block - requires 3 lines");
        }

        String timeLine = lines.get(startIndex);
        String nameLine = lines.get(startIndex + 1);
        String messageLine = lines.get(startIndex + 2);

        // Validate line prefixes
        if (!timeLine.startsWith("Time:")) {
            throw new ValidationException("Invalid timestamp line format - must start with 'Time:'");
        }
        if (!nameLine.startsWith("Name:")) {
            throw new ValidationException("Invalid name line format - must start with 'Name:'");
        }
        if (!messageLine.startsWith("Message:")) {
            throw new ValidationException("Invalid message line format - must start with 'Message:'");
        }

        // Extract and validate fields
        String timestamp = validateTimestamp(timeLine.substring(5).trim());
        String nickname = validateNickname(nameLine.substring(5).trim());
        String content = validateMessageContent(messageLine.substring(8));

        return new ChatMessage(timestamp, nickname, content);
    }

    /**
     * Validates timestamp according to ISO 8601 format and length constraints.
     *
     * @param timestamp the timestamp string to validate
     * @return normalized timestamp
     * @throws ValidationException if validation fails
     */
    private String validateTimestamp(String timestamp) throws ValidationException {
        // Check length constraint
        if (timestamp.length() > MAX_FIELD_LENGTH) {
            throw new ValidationException("Timestamp exceeds maximum length of " + MAX_FIELD_LENGTH + " characters");
        }

        // Check for empty timestamp
        if (timestamp.isEmpty()) {
            throw new ValidationException("Timestamp cannot be empty");
        }

        // Strict ISO 8601 validation
        try {
            // Try to parse as ISO 8601 format
            ISO_8601_FORMATTER.parse(timestamp);
            return timestamp;
        } catch (DateTimeParseException e) {
            throw new ValidationException("Malformed timestamp format - must be ISO 8601 format: " + e.getMessage());
        }
    }

    /**
     * Validates nickname according to character restrictions and length constraints.
     *
     * @param nickname the nickname to validate
     * @return normalized nickname
     * @throws ValidationException if validation fails
     */
    private String validateNickname(String nickname) throws ValidationException {
        // Check length constraint
        if (nickname.length() > MAX_FIELD_LENGTH) {
            throw new ValidationException("Nickname exceeds maximum length of " + MAX_FIELD_LENGTH + " characters");
        }

        // Check for empty nickname
        if (nickname.isEmpty()) {
            throw new ValidationException("Nickname cannot be empty");
        }

        // Check for whitespace-only nickname
        if (WHITESPACE_ONLY_PATTERN.matcher(nickname).matches()) {
            throw new ValidationException("Nickname cannot contain only whitespace");
        }

        // Validate character set: alphanumeric, underscore, hyphen, period
        if (!VALID_NICKNAME_PATTERN.matcher(nickname).matches()) {
            throw new ValidationException("Nickname contains invalid characters - only A-Z, a-z, 0-9, _, -, . allowed");
        }

        return nickname;
    }

    /**
     * Validates message content with UTF-8 encoding and Unicode normalization.
     *
     * @param content the message content to validate
     * @return normalized and validated content
     * @throws ValidationException if validation fails
     */
    private String validateMessageContent(String content) throws ValidationException {
        // Check length constraint
        if (content.length() > MAX_FIELD_LENGTH) {
            throw new ValidationException("Message content exceeds maximum length of " + MAX_FIELD_LENGTH + " characters");
        }

        // Note: Empty messages are allowed according to specifications
        // But whitespace-only messages are rejected
        if (!content.isEmpty() && WHITESPACE_ONLY_PATTERN.matcher(content).matches()) {
            throw new ValidationException("Message content cannot contain only whitespace");
        }

        // Normalize Unicode text to NFC (Canonical Decomposition + Canonical Composition)
        String normalizedContent = Normalizer.normalize(content, Normalizer.Form.NFC);

        // Validate UTF-8 encoding by attempting to encode/decode
        try {
            byte[] utf8Bytes = normalizedContent.getBytes(StandardCharsets.UTF_8);
            String decoded = new String(utf8Bytes, StandardCharsets.UTF_8);
            if (!normalizedContent.equals(decoded)) {
                throw new ValidationException("Message content contains invalid UTF-8 sequences");
            }
        } catch (Exception e) {
            throw new ValidationException("Message content encoding validation failed: " + e.getMessage());
        }

        return normalizedContent;
    }
}