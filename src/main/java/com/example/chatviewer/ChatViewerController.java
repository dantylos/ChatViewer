package com.example.chatviewer;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Controller for the Chat Viewer UI. Handles loading and displaying messages.
public class ChatViewerController {

    @FXML
    private Button loadButton;

    @FXML
    private Label filePathLabel;

    @FXML
    private TextFlow textFlow;

    @FXML
    private ScrollPane scrollPane;

    private File lastDirectory;

    private Image smileImage;
    private Image sadImage;

    // Pattern to find emoticons :) and :(
    private static final Pattern EMOTICON_PATTERN = Pattern.compile("(:\\)|:\\()");

    // Called after the FXML is loaded. Initializes resources.
    @FXML
    public void initialize() {
        // Try to load emoticon images from resources, use fallback if not found
        try {
            smileImage = new Image(getClass().getResourceAsStream("/images/smile.png"));
            sadImage = new Image(getClass().getResourceAsStream("/images/sad.png"));
        } catch (Exception e) {
            // Create simple text-based fallback images
            System.out.println("Warning: Emoticon images not found, using text fallbacks");
            smileImage = null;
            sadImage = null;
        }

        // Initialize last directory to user's home
        lastDirectory = new File(System.getProperty("user.home"));

        // Set up scroll pane for TextFlow
        if (scrollPane != null) {
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
        }
    }

    // Handler for the Load File button. Opens a FileChooser and displays messages.
    @FXML
    private void handleLoad(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Message File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Message Files", "*.msg"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        if (lastDirectory != null && lastDirectory.exists()) {
            fileChooser.setInitialDirectory(lastDirectory);
        }

        File selectedFile = fileChooser.showOpenDialog(loadButton.getScene().getWindow());
        if (selectedFile != null) {
            lastDirectory = selectedFile.getParentFile();
            filePathLabel.setText(selectedFile.getAbsolutePath());

            try {
                List<Message> messages = parseMessages(selectedFile);
                displayMessages(messages);
            } catch (Exception e) {
                showAlert("Could not read or parse file:\n" + e.getMessage());
                e.printStackTrace(); // Debugging
            }
        }
    }

    /**
     * Parses the .msg file into a list of Message objects.
     * @param file the message file
     * @return list of parsed Message objects
     * @throws IOException if reading the file fails
     * @throws IllegalArgumentException if file format is invalid
     */
    private List<Message> parseMessages(File file) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());
        List<Message> messages = new ArrayList<>();

        if (lines.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Parse messages in groups of 3 lines separated by empty lines
        for (int i = 0; i < lines.size(); ) {
            while (i < lines.size() && lines.get(i).trim().isEmpty()) {
                i++;
            }

            // Check if we have at least 3 lines for a complete message
            if (i + 2 >= lines.size()) {
                break;
            }

            try {
                String timeLine = lines.get(i);
                String nameLine = lines.get(i + 1);
                String messageLine = lines.get(i + 2);

                Message message = new Message(timeLine, nameLine, messageLine);
                messages.add(message);

                i += 3;
            } catch (Exception e) {
                throw new IllegalArgumentException("Error parsing message at line " + (i + 1) + ": " + e.getMessage());
            }
        }

        if (messages.isEmpty()) {
            throw new IllegalArgumentException("No valid messages found in file");
        }

        return messages;
    }

    /**
     * Displays a list of messages in the TextFlow, applying styles and emoticons.
     * @param messages the list of Message objects
     */
    private void displayMessages(List<Message> messages) {
        textFlow.getChildren().clear();

        String previousNickname = null;

        for (Message msg : messages) {
            // Timestamp in brackets
            Text tsText = new Text("[" + msg.getTimestamp() + "] ");
            textFlow.getChildren().add(tsText);

            // Nickname logic: show "..." if same as previous, otherwise show nickname in blue
            String displayNickname;
            if (msg.getNickname().equals(previousNickname)) {
                displayNickname = "...";
            } else {
                displayNickname = msg.getNickname();
                previousNickname = msg.getNickname();
            }

            Text nickText = new Text(displayNickname + ": ");
            nickText.setFill(Color.BLUE);
            textFlow.getChildren().add(nickText);

            // Message content with bold style and emoticons
            String content = msg.getContent();
            processContentWithEmoticons(content);

            Text newline = new Text("\n");
            textFlow.getChildren().add(newline);
        }
    }

    // Processes message content, replacing emoticons with images and applying bold style.
    private void processContentWithEmoticons(String content) {
        Matcher matcher = EMOTICON_PATTERN.matcher(content);
        int lastIndex = 0;

        while (matcher.find()) {
            // Add text before emoticon
            if (matcher.start() > lastIndex) {
                String textSegment = content.substring(lastIndex, matcher.start());
                Text textNode = new Text(textSegment);
                textNode.setStyle("-fx-font-weight: bold;");
                textFlow.getChildren().add(textNode);
            }

            // Add emoticon
            String emoticon = matcher.group();
            addEmoticon(emoticon);

            lastIndex = matcher.end();
        }

        // Add remaining text after last emoticon
        if (lastIndex < content.length()) {
            String remainingText = content.substring(lastIndex);
            Text textNode = new Text(remainingText);
            textNode.setStyle("-fx-font-weight: bold;");
            textFlow.getChildren().add(textNode);
        }
    }

    // Adds an emoticon to the TextFlow (image or text fallback).
    private void addEmoticon(String emoticon) {
        if (emoticon.equals(":)") && smileImage != null) {
            ImageView imageView = new ImageView(smileImage);
            imageView.setFitWidth(16);
            imageView.setFitHeight(16);
            imageView.setPreserveRatio(true);
            textFlow.getChildren().add(imageView);
        } else if (emoticon.equals(":(") && sadImage != null) {
            ImageView imageView = new ImageView(sadImage);
            imageView.setFitWidth(16);
            imageView.setFitHeight(16);
            imageView.setPreserveRatio(true);
            textFlow.getChildren().add(imageView);
        } else {
            // Fallback to text if images not available
            Text emoticonText = new Text(emoticon);
            emoticonText.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            textFlow.getChildren().add(emoticonText);
        }
    }

    /**
     * Shows an alert dialog with a given title and message.
     * @param message the alert message
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("File Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setPrefWidth(400);
        alert.showAndWait();
    }
}