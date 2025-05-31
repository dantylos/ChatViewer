package com.chatviewer.controller;

import com.chatviewer.model.ChatMessage;
import com.chatviewer.util.ErrorHandler;
import com.chatviewer.util.MessageParser;
import com.chatviewer.util.MessageRenderer;
import com.chatviewer.util.ValidationException;
import com.chatviewer.view.MainView;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Main controller managing interactions between the view and model components.
 * Handles file selection, conversation loading, and UI updates with comprehensive error handling.
 *
 * @author Chat Viewer Team
 * @version 1.0.0
 * @since 2025-05-31
 */
public class MainController {

    private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());

    /** The main view this controller manages */
    private final MainView view;

    /** Parser for processing .msg files */
    private final MessageParser parser = new MessageParser();

    /** Renderer for converting messages to UI components */
    private final MessageRenderer renderer = new MessageRenderer();

    /** Last directory for file chooser memory */
    private Path lastDirectory = null;

    /**
     * Constructs the controller and sets up event handlers.
     * Initializes the MVC connection between view and controller.
     *
     * @param view the main view to control
     */
    public MainController(MainView view) {
        this.view = view;
        setupHandlers();
        LOGGER.info("MainController initialized successfully");
    }

    /**
     * Configures event handlers for UI components.
     * Sets up file open dialog and other user interactions.
     */
    private void setupHandlers() {
        view.getOpenButton().setOnAction(e -> openFileDialog());
    }

    /**
     * Opens a file chooser dialog to select a conversation file.
     * Remembers the last opened directory for user convenience.
     */
    private void openFileDialog() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Chat Conversation");

            // Set file filter for .msg files
            FileChooser.ExtensionFilter msgFilter = new FileChooser.ExtensionFilter(
                    "Chat message files (*.msg)", "*.msg");
            fileChooser.getExtensionFilters().add(msgFilter);

            // Remember last directory for user convenience
            if (lastDirectory != null && lastDirectory.toFile().exists()) {
                fileChooser.setInitialDirectory(lastDirectory.toFile());
            }

            // Show file chooser dialog
            File selectedFile = fileChooser.showOpenDialog(view.getScene().getWindow());

            if (selectedFile != null) {
                // Update last directory
                lastDirectory = selectedFile.getParentFile().toPath();

                // Update UI with selected file path
                view.getPathLabel().setText(selectedFile.getAbsolutePath());

                // Load and display the conversation
                loadConversationWithValidation(selectedFile.toPath());

                LOGGER.info("File selected: " + selectedFile.getAbsolutePath());
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in file selection dialog", e);
            ErrorHandler.showError(
                    "Dialog Error",
                    "Error opening file selection dialog",
                    "An unexpected error occurred while opening the file dialog: " + e.getMessage()
            );
        }
    }

    /**
     * Loads and displays the conversation from the specified file with comprehensive validation.
     * Handles all types of errors and provides appropriate user feedback.
     *
     * @param file the path to the conversation file
     */
    private void loadConversationWithValidation(Path file) {
        try {
            LOGGER.info("Loading conversation file: " + file.toString());

            // Clear previous content
            view.getMessageFlow().getChildren().clear();

            // Parse the file with validation
            List<ChatMessage> messages = parser.parse(file);

            // Reset renderer state for new conversation
            renderer.resetNicknameTracking();

            // Render each message and add to UI
            for (ChatMessage message : messages) {
                view.getMessageFlow().getChildren().add(renderer.renderMessage(message));
            }

            LOGGER.info("Successfully loaded and displayed " + messages.size() + " messages");

        } catch (ValidationException ex) {
            LOGGER.log(Level.WARNING, "Validation error while parsing file: " + ex.getMessage(), ex);
            ErrorHandler.showValidationError(
                    "File Validation Error",
                    "The file format is incorrect!",
                    "The chat file does not meet the required format specifications",
                    ex
            );
            // Clear the file path on error
            view.getPathLabel().setText("Error loading file");

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "IO error while reading file: " + ex.getMessage(), ex);
            ErrorHandler.showFileError(file.toString(), ex);
            view.getPathLabel().setText("Error reading file");

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Unexpected error during file loading: " + ex.getMessage(), ex);
            ErrorHandler.showError(
                    "Unexpected Error",
                    "An unexpected error occurred",
                    "Please check the application logs for more details.\n\nError: " + ex.getMessage()
            );
            view.getPathLabel().setText("Unexpected error");
        }
    }
}
