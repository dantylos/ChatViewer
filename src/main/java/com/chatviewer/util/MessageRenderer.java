package com.chatviewer.util;

import com.chatviewer.model.ChatMessage;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Renders ChatMessage objects into styled JavaFX TextFlow nodes.
 * Handles emoticon replacement, text formatting, and nickname repetition logic.
 *
 * @author Chat Viewer Team
 * @version 1.0.0
 * @since 2025-05-31
 */
public class MessageRenderer {

    private static final Logger LOGGER = Logger.getLogger(MessageRenderer.class.getName());

    /** Tracks the previous message nickname for ellipsis replacement */
    private String previousNickname = "";

    /** Happy emoticon image */
    private final Image happyEmoticon;

    /** Sad emoticon image */
    private final Image sadEmoticon;

    /**
     * Constructs a MessageRenderer and loads emoticon images from resources.
     *
     * @throws RuntimeException if emoticon images cannot be loaded
     */
    public MessageRenderer() {
        try {
            happyEmoticon = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream("/images/smile_happy.png")));
            sadEmoticon = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream("/images/smile_sad.png")));
            LOGGER.info("Successfully loaded emoticon images");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load emoticon images", e);
            throw new RuntimeException("Failed to load required emoticon images", e);
        }
    }

    /**
     * Renders a single ChatMessage as a TextFlow node with formatted elements.
     * Applies formatting rules: timestamps in brackets, blue nicknames, bold content, emoticon images.
     *
     * @param message the chat message to render
     * @return a TextFlow node representing the formatted message line
     */
    public Node renderMessage(ChatMessage message) {
        TextFlow flow = new TextFlow();

        try {
            // TIME_STAMP: surrounded by square brackets
            Text timestampText = new Text("[" + message.timestamp() + "]");
            timestampText.setStyle("-fx-fill: black;");
            flow.getChildren().add(timestampText);

            // NICK_NAME: blue text + colon, or "..." if same as previous
            if (!message.nickname().equals(previousNickname)) {
                Text nicknameText = new Text(message.nickname() + ":");
                nicknameText.setStyle("-fx-fill: blue;");
                flow.getChildren().add(nicknameText);
                previousNickname = message.nickname();
            } else {
                Text ellipsisText = new Text("...:");
                ellipsisText.setStyle("-fx-fill: blue;");
                flow.getChildren().add(ellipsisText);
            }

            // CONTENT: black, bold text with emoticons replaced by images
            addContentWithEmoticons(message.content(), flow);

            LOGGER.fine("Successfully rendered message for nickname: " + message.nickname());

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error rendering message", e);
            // Fallback: show plain text
            Text errorText = new Text("Error rendering message: " + message.nickname() + ": " + message.content());
            flow.getChildren().add(errorText);
        }

        return flow;
    }

    /**
     * Processes message content, replacing emoticons with images and styling text.
     * Handles :) and :( emoticons by replacing them with corresponding images.
     *
     * @param content the message content string
     * @param flow the TextFlow container to add nodes to
     */
    private void addContentWithEmoticons(String content, TextFlow flow) {
        if (content.isEmpty()) {
            return;
        }

        // Split content by emoticon occurrences ":)" and ":("
        // Use lookahead and lookbehind to preserve emoticon text during split
        String[] parts = content.split("(?=:\\))|(?=:\\()|(?<=:\\))|(?<=:\\()");

        for (String part : parts) {
            if (part.equals(":)")) {
                ImageView happyView = new ImageView(happyEmoticon);
                happyView.setFitHeight(16);
                happyView.setFitWidth(16);
                happyView.setPreserveRatio(true);
                flow.getChildren().add(happyView);
            } else if (part.equals(":(")) {
                ImageView sadView = new ImageView(sadEmoticon);
                sadView.setFitHeight(16);
                sadView.setFitWidth(16);
                sadView.setPreserveRatio(true);
                flow.getChildren().add(sadView);
            } else if (!part.isEmpty()) {
                Text text = new Text(part);
                text.setStyle("-fx-fill: black; -fx-font-weight: bold;");
                flow.getChildren().add(text);
            }
        }
    }

    /**
     * Resets the nickname tracking for new conversation rendering.
     * Should be called when loading a new conversation.
     */
    public void resetNicknameTracking() {
        previousNickname = "";
        LOGGER.fine("Reset nickname tracking");
    }
}
