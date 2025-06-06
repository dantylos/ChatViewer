package com.example.chatviewer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Main JavaFX application class for the Chat Viewer.
public class ChatViewerApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/chat_viewer.fxml"));
        Scene scene = new Scene(loader.load(), 500, 700);
        primaryStage.setTitle("Chat Viewer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Launches the application.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}