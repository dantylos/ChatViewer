package com.chatviewer.util;

import java.io.Serial;

/**
 * Custom exception for validation errors in message parsing.
 * Provides detailed information about validation failures for debugging and user feedback.
 *
 * @author Chat Viewer Team
 * @version 1.0.0
 * @since 2025-05-31
 */
public class ValidationException extends Exception {

    /** Serial version UID for serialization */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a ValidationException with the specified detail message.
     *
     * @param message the detail message explaining the validation failure
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructs a ValidationException with the specified detail message and cause.
     *
     * @param message the detail message explaining the validation failure
     * @param cause the underlying cause of the validation failure
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
