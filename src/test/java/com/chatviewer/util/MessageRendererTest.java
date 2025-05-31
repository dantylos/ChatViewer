package com.chatviewer.util;

import com.chatviewer.model.ChatMessage;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.text.TextFlow;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the MessageRenderer.
 */
class MessageRendererTest {

    @BeforeAll
    static void initToolkit() {
        // Initializes JavaFX toolkit for testing
        new JFXPanel();
    }

    @Test
    void testRenderMessageWithEmoticons() {
        MessageRenderer renderer = new MessageRenderer();
        ChatMessage msg = new ChatMessage("2025-05-31T12:00:00", "Alice", "Hello :) How are you :(");

        Node node = renderer.renderMessage(msg);
        assertTrue(node instanceof TextFlow);
        TextFlow flow = (TextFlow) node;

        boolean hasEmoticon = flow.getChildren().stream().anyMatch(n -> n instanceof javafx.scene.image.ImageView);
        assertTrue(hasEmoticon, "Should contain at least one emoticon image");
    }

    @Test
    void testRenderMessageNicknameEllipsis() {
        MessageRenderer renderer = new MessageRenderer();

        ChatMessage msg1 = new ChatMessage("2025-05-31T12:00:00", "Alice", "First");
        ChatMessage msg2 = new ChatMessage("2025-05-31T12:01:00", "Alice", "Second");

        renderer.renderMessage(msg1); // sets previousNickname
        Node node = renderer.renderMessage(msg2);

        assertTrue(node instanceof TextFlow);
        TextFlow flow = (TextFlow) node;

        boolean hasEllipsis = flow.getChildren().stream()
                .anyMatch(n -> n instanceof javafx.scene.text.Text && ((javafx.scene.text.Text) n).getText().contains("..."));
        assertTrue(hasEllipsis, "Should contain ellipsis for repeated nickname");
    }
}
