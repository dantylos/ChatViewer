package com.chatviewer;

import com.chatviewer.controller.MainController;
import com.chatviewer.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main JavaFX application class for the Chat Viewer.
 * Initializes the application window and sets up the MVC architecture.
 *
 * @author Chat Viewer Team
 * @version 1.0.0
 * @since 2025-05-31
 */
public class Main extends Application {

    /**
     * Starts the JavaFX application by creating the main view and controller.
     * Sets up the primary stage with appropriate title and scene.
     *
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Create view and controller following MVC pattern
            MainView view = new MainView();
            new MainController(view);

            // Setup scene and stage
            Scene scene = new Scene(view, 800, 600);
            primaryStage.setTitle("Chat Viewer");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(400);
            primaryStage.setMinHeight(300);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Failed to start application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Main method to launch the JavaFX application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
