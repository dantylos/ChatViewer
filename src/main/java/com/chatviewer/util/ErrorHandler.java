package com.chatviewer.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Enhanced error handler with logging capabilities and detailed user feedback.
 * Provides comprehensive error dialogs with recovery suggestions and technical details.
 *
 * @author Chat Viewer Team
 * @version 1.0.0
 * @since 2025-05-31
 */
public class ErrorHandler {

    private static final Logger LOGGER = Logger.getLogger(ErrorHandler.class.getName());

    /**
     * Shows a validation error dialog with detailed information and logs the error.
     * Provides specific guidance for validation failures.
     *
     * @param title the dialog window title
     * @param header the header text
     * @param content the detailed message content
     * @param exception the validation exception that occurred
     */
    public static void showValidationError(String title, String header, String content, ValidationException exception) {
        // Log the validation failure for debugging
        LOGGER.log(Level.WARNING, "Validation error: " + content, exception);

        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);

        String fullContent = content + "\n\nTechnical details: " + exception.getMessage() +
                "\n\nPlease ensure your .msg file follows the correct format:\n" +
                "• Timestamps must be in ISO 8601 format (YYYY-MM-DDTHH:MM:SS)\n" +
                "• Nicknames can only contain letters, numbers, underscore, hyphen, period\n" +
                "• Each field must be under 1000 characters\n" +
                "• File must be UTF-8 encoded";

        alert.setContentText(fullContent);
        alert.showAndWait();
    }

    /**
     * Shows a general error dialog with the specified title, header, and content.
     * Includes expandable details for technical information.
     *
     * @param title the dialog window title
     * @param header the header text
     * @param content the detailed message content
     */
    public static void showError(String title, String header, String content) {
        LOGGER.log(Level.SEVERE, "Error: " + content);

        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Shows a file-related error with recovery suggestions and expandable stack trace.
     *
     * @param filePath the path of the problematic file
     * @param exception the exception that occurred
     */
    public static void showFileError(String filePath, Exception exception) {
        LOGGER.log(Level.SEVERE, "File error for: " + filePath, exception);

        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("File Processing Error");
        alert.setHeaderText("Could not process chat file");

        String content = String.format(
                "Error processing file: %s\n\n" +
                        "Error details: %s\n\n" +
                        "Suggestions:\n" +
                        "• Ensure the file is UTF-8 encoded\n" +
                        "• Check file permissions and accessibility\n" +
                        "• Verify the .msg file format is correct\n" +
                        "• Ensure timestamps are in ISO 8601 format\n" +
                        "• Check that nicknames only contain valid characters",
                filePath, exception.getMessage()
        );

        alert.setContentText(content);

        // Add expandable exception details
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String exceptionText = sw.toString();

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 0);

        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }
}
