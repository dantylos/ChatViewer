package com.chatViewer;

import com.chatViewer.controller.MainController;
import com.chatViewer.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/* Main JavaFX application class that initializes and shows the chat viewer window. */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainView view = new MainView();
        new MainController(view);

        Scene scene = new Scene(view, 800, 600);
        primaryStage.setTitle("Chat Viewer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /* Main method to launch the JavaFX application. @param args command-line arguments */
    public static void main(String[] args) {
        launch(args);
    }
}
