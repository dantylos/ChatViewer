package com.chatViewer.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Utility class to show error dialogs to inform the user about problems.
 */
public class ErrorHandler {

    /**
     * Shows an error dialog with the specified title, header, and content.
     *
     * @param title   the dialog window title
     * @param header  the header text
     * @param content the detailed message content
     */
    public static void showError(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}