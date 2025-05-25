package com.chatViewer.view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

/* The main view of the chat viewer application containing UI components. */
public class MainView extends BorderPane {

    private final TextFlow messageFlow = new TextFlow();
    private final Button openButton = new Button("Open Chat");
    private final Label pathLabel = new Label("No file opened");

    /* Constructs the main view and sets up the layout */
    public MainView() {
        setupLayout();
    }

    /* Sets up the UI layout and adds components. */
    private void setupLayout() {
        // Controls container
        VBox controls = new VBox(10, openButton, pathLabel);
        controls.setPadding(new Insets(10));
        setTop(controls);

        // Scrollable message display area
        ScrollPane scrollPane = new ScrollPane(messageFlow);
        scrollPane.setFitToWidth(true);
        setCenter(scrollPane);

        // Padding for the message flow
        messageFlow.setPadding(new Insets(10));
    }

    /**
     * Returns the TextFlow component where messages are displayed.
     *
     * @return the TextFlow component
     */
    public TextFlow getMessageFlow() {
        return messageFlow;
    }

    /**
     * Returns the Button used to open files.
     *
     * @return the open file Button
     */
    public Button getOpenButton() {
        return openButton;
    }

    /**
     * Returns the Label that shows the opened file path.
     *
     * @return the Label displaying file path
     */
    public Label getPathLabel() {
        return pathLabel;
    }
}