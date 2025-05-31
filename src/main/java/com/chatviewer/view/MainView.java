package com.chatviewer.view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

/**
 * The main view of the chat viewer application containing UI components.
 * Implements responsive design with proper layout management and scrolling support.
 *
 * @author Chat Viewer Team
 * @version 1.0.0
 * @since 2025-05-31
 */
public class MainView extends BorderPane {

    /** TextFlow component for displaying formatted chat messages */
    private final TextFlow messageFlow = new TextFlow();

    /** Button for opening file chooser dialog */
    private final Button openButton = new Button("Open Chat");

    /** Label for displaying currently opened file path */
    private final Label pathLabel = new Label("No file opened");

    /**
     * Constructs the main view and sets up the responsive layout.
     */
    public MainView() {
        setupLayout();
        configureComponents();
    }

    /**
     * Sets up the UI layout with proper spacing and responsive design.
     * Arranges components in a BorderPane with controls at top and scrollable content in center.
     */
    private void setupLayout() {
        // Controls container at the top
        VBox controls = new VBox(10);
        controls.getChildren().addAll(openButton, pathLabel);
        controls.setPadding(new Insets(10));
        setTop(controls);

        // Scrollable message display area in center
        ScrollPane scrollPane = new ScrollPane(messageFlow);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        setCenter(scrollPane);

        // Padding for the message flow
        messageFlow.setPadding(new Insets(10));

        // Ensure responsive behavior
        this.setPadding(new Insets(5));
    }

    /**
     * Configures component properties for better user experience.
     */
    private void configureComponents() {
        // Style the open button
        openButton.setPrefWidth(100);
        openButton.setStyle("-fx-font-size: 14px;");

        // Style the path label
        pathLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666666;");
        pathLabel.setWrapText(true);

        // Configure message flow
        messageFlow.setLineSpacing(5);
    }

    /**
     * Returns the TextFlow component where messages are displayed.
     *
     * @return the TextFlow component for message display
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
     * @return the Label displaying file path information
     */
    public Label getPathLabel() {
        return pathLabel;
    }
}
