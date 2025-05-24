package com.chatViewer.util;

import com.chatViewer.model.ChatMessage;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.Objects;

/* Renders ChatMessage objects into styled JavaFX TextFlow nodes, replacing emoticon strings with images and applying formatting rules. */
public class MessageRenderer {

    private String previousNickname = "";

    private final Image happyEmoticon;
    private final Image sadEmoticon;

    /* Loads emoticon images from resources. */
    public MessageRenderer() {
        happyEmoticon = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("../resources/emoticons/happy-smile.png")));
        sadEmoticon = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("../resources/emoticons/happy-smile.png")));
    }

    /**
     * Renders a single ChatMessage as a TextFlow node with formatted elements.
     *
     * @param message the chat message to render
     * @return a TextFlow node representing the formatted message line
     */
    public Node renderMessage(ChatMessage message) {
        TextFlow flow = new TextFlow();

        // TIME_STAMP: surrounded by square brackets
        Text timestampText = new Text("[" + message.timestamp() + "]");
        flow.getChildren().add(timestampText);

        // NICK_NAME: blue text + colon, or "..." if same as previous
        if (!message.nickname().equals(previousNickname)) {
            Text nicknameText = new Text(message.nickname() + ":");
            nicknameText.setStyle("-fx-fill: blue;");
            flow.getChildren().add(nicknameText);
            previousNickname = message.nickname();
        } else {
            Text ellipsisText = new Text("...");
            ellipsisText.setStyle("-fx-fill: blue;");
            flow.getChildren().add(ellipsisText);
        }

        // CONTENT: black, bold text with emoticons replaced by images
        addContentWithEmoticons(message.content(), flow);

        return flow;
    }

    /**
     * Processes message content, replacing emoticons with images and styling text.
     *
     * @param content the message content string
     * @param flow    the TextFlow container to add nodes to
     */
    private void addContentWithEmoticons(String content, TextFlow flow) {
        // Split content by emoticon occurrences ":)" and ":("
        String[] parts = content.split("(?=:\\))|(?=:\\()|(?<=:\\))|(?<=:\\()");

        for (String part : parts) {
            switch (part) {
                case ":)":
                    ImageView happyView = new ImageView(happyEmoticon);
                    happyView.setFitHeight(16);
                    happyView.setFitWidth(16);
                    flow.getChildren().add(happyView);
                    break;
                case ":(":
                    ImageView sadView = new ImageView(sadEmoticon);
                    sadView.setFitHeight(16);
                    sadView.setFitWidth(16);
                    flow.getChildren().add(sadView);
                    break;
                default:
                    Text text = new Text(part);
                    text.setStyle("-fx-fill: black; -fx-font-weight: bold;");
                    flow.getChildren().add(text);
                    break;
            }
        }
    }
}