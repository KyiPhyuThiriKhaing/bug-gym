package dev.philixtheexplorer.buggym.ui;

import dev.philixtheexplorer.buggym.application.AppController;
import dev.philixtheexplorer.buggym.model.Category;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;

import java.util.Collection;

/**
 * Coordinates shared UI state transitions for the workspace/home shell.
 */
public class WorkspaceUiCoordinator {

    public void updateProgressLabel(Label progressLabel, AppController.ProgressSnapshot snapshot) {
        progressLabel.setText("Progress: %d/%d solved"
                .formatted(snapshot.solvedQuestions(), snapshot.totalQuestions()));
    }

    public void refreshHomeCategories(HomePageView homeContainer, Collection<Category> categories) {
        homeContainer.refreshCategories(categories);
    }

    public void showHomePage(HomePageView homeContainer, SplitPane mainContentSplit, Collection<Category> categories) {
        homeContainer.refreshCategories(categories);
        homeContainer.setVisible(true);
        homeContainer.setManaged(true);
        mainContentSplit.setVisible(false);
        mainContentSplit.setManaged(false);
    }

    public void showPracticePage(HomePageView homeContainer, SplitPane mainContentSplit) {
        homeContainer.setVisible(false);
        homeContainer.setManaged(false);
        mainContentSplit.setVisible(true);
        mainContentSplit.setManaged(true);
    }

    public void applyTheme(Scene scene, boolean darkMode) {
        if (darkMode) {
            scene.getRoot().getStyleClass().remove("light-mode");
        } else {
            scene.getRoot().getStyleClass().add("light-mode");
        }
    }
}
