package com.chatViewer.controller;

import com.chatViewer.model.ChatMessage;
import com.chatViewer.util.ErrorHandler;
import com.chatViewer.util.MessageParser;
import com.chatViewer.util.MessageRenderer;
import com.chatViewer.view.MainView;
import javafx.stage.FileChooser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Controller class managing interactions between the view and model.
 */
public class MainController {

    private final MainView view;
    private final MessageParser parser = new MessageParser();
    private final MessageRenderer renderer = new MessageRenderer();

    private Path lastDirectory = null;

    /**
     * Constructs the controller and sets up event handlers.
     *
     * @param view the main view to control
     */
    public MainController(MainView view) {
        this.view = view;
        setupHandlers();
    }

    /**
     * Configures event handlers for UI components.
     */
    private void setupHandlers() {
        view.getOpenButton().setOnAction(e -> openFileDialog());
    }

    /**
     * Opens a file chooser dialog to select a conversation file, parses it,
     * and updates the UI accordingly.
     */
    private void openFileDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Chat Conversation");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Chat message files (*.msg)", "*.msg"));

        if (lastDirectory != null) {
            fileChooser.setInitialDirectory(lastDirectory.toFile());
        }

        var file = fileChooser.showOpenDialog(view.getScene().getWindow());
        if (file != null) {
            lastDirectory = file.getParentFile().toPath();
            view.getPathLabel().setText(file.getAbsolutePath());
            loadConversation(file.toPath());
        }
    }

    /**
     * Loads and displays the conversation from the specified file.
     *
     * @param file the path to the conversation file
     */
    private void loadConversation(Path file) {
        try {
            List<ChatMessage> messages = parser.parse(file);
            view.getMessageFlow().getChildren().clear();

            // Reset renderer state for nickname repetition logic
            renderer.previousNickname = "";

            for (ChatMessage message : messages) {
                view.getMessageFlow().getChildren().add(renderer.renderMessage(message));
            }
        } catch (IOException ex) {
            ErrorHandler.showError("File Read Error", "Could not read file", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            ErrorHandler.showError("File Format Error", "Invalid file format", ex.getMessage());
        } catch (Exception ex) {
            ErrorHandler.showError("Unexpected Error", "An unexpected error occurred", ex.getMessage());
        }
    }
}