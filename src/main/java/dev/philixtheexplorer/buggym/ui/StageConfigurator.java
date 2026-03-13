package dev.philixtheexplorer.buggym.ui;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Applies stage/window configuration for the main app shell.
 */
public class StageConfigurator {

    public void configureMainStage(Stage stage, Scene scene, Runnable onClose) {
        stage.setTitle("Bug Gym - Java Practice Platform");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/bug-gym.png")));
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setMaximized(true);

        stage.setOnCloseRequest(event -> onClose.run());
    }
}
