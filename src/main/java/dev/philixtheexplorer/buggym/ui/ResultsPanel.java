package dev.philixtheexplorer.buggym.ui;

import dev.philixtheexplorer.buggym.model.RunResult;
import dev.philixtheexplorer.buggym.model.TestResult;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;

/**
 * Panel for displaying test results.
 */
public class ResultsPanel extends VBox {

    private final Label summaryLabel;
    private final VBox resultsContainer;
    private final ScrollPane scrollPane;

    public ResultsPanel() {
        setSpacing(10);
        setPadding(new Insets(10));
        getStyleClass().add("results-panel");

        summaryLabel = new Label("Run your code to see results");
        summaryLabel.getStyleClass().add("results-summary");
        summaryLabel.setWrapText(true);

        resultsContainer = new VBox(2);
        resultsContainer.getStyleClass().add("results-container");

        scrollPane = new ScrollPane(resultsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.getStyleClass().add("results-scroll-pane");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        getChildren().addAll(summaryLabel, scrollPane);
    }

    /**
     * Displays the results of a test run.
     */
    public void showResults(RunResult result) {
        if (result == null) {
            clear();
            return;
        }

        // Update summary
        updateSummary(result);

        // Clear previous results
        resultsContainer.getChildren().clear();

        if (!result.compilationSuccess()) {
            Label errorLabel = new Label("Passed compilation failed:\n" + result.compilationError());
            errorLabel.getStyleClass().add("compilation-error");
            errorLabel.setWrapText(true);
            resultsContainer.getChildren().add(errorLabel);
            return;
        }

        // Create table header
        resultsContainer.getChildren().add(createHeaderRow());

        // Create result rows
        for (TestResult testResult : result.testResults()) {
            resultsContainer.getChildren().add(createResultRow(testResult));
        }
    }

    private void updateSummary(RunResult result) {
        if (!result.compilationSuccess()) {
            summaryLabel.setText("❌ Compilation Failed");
            summaryLabel.getStyleClass().removeAll("success", "failure");
            summaryLabel.getStyleClass().add("failure");
        } else if (result.allPassed()) {
            summaryLabel.setText("🎉 All %d tests passed!".formatted(result.totalCount()));
            summaryLabel.getStyleClass().removeAll("success", "failure");
            summaryLabel.getStyleClass().add("success");
        } else {
            summaryLabel.setText("📊 %d/%d tests passed".formatted(result.passedCount(), result.totalCount()));
            summaryLabel.getStyleClass().removeAll("success", "failure");
            summaryLabel.getStyleClass().add("failure");
        }
    }

    private HBox createHeaderRow() {
        HBox row = new HBox(10);
        row.getStyleClass().add("result-header-row");
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(8, 10, 8, 10));

        Label statusHeader = new Label("Status");
        statusHeader.setMinWidth(50);

        Label actionHeader = new Label("Action");
        actionHeader.setMinWidth(80);
        actionHeader.setAlignment(Pos.CENTER);

        Label inputHeader = new Label("Input");
        inputHeader.setPrefWidth(1);
        inputHeader.setMaxWidth(Double.MAX_VALUE);
        inputHeader.setAlignment(Pos.CENTER_LEFT);
        inputHeader.setStyle("-fx-alignment: center-left;");
        HBox.setHgrow(inputHeader, Priority.ALWAYS);

        Label expectedHeader = new Label("Expected");
        expectedHeader.setPrefWidth(1);
        expectedHeader.setMaxWidth(Double.MAX_VALUE);
        expectedHeader.setAlignment(Pos.CENTER_LEFT);
        expectedHeader.setStyle("-fx-alignment: center-left;");
        HBox.setHgrow(expectedHeader, Priority.ALWAYS);

        Label gotHeader = new Label("Got");
        gotHeader.setPrefWidth(1);
        gotHeader.setMaxWidth(Double.MAX_VALUE);
        gotHeader.setAlignment(Pos.CENTER_LEFT);
        gotHeader.setStyle("-fx-alignment: center-left;");
        HBox.setHgrow(gotHeader, Priority.ALWAYS);

        row.getChildren().addAll(statusHeader, actionHeader, inputHeader, expectedHeader, gotHeader);
        return row;
    }

    private HBox createResultRow(TestResult result) {
        HBox row = new HBox(10);
        row.getStyleClass().add("result-row");
        if (!result.passed()) {
            row.getStyleClass().add("result-row-failed");
        }
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(8, 10, 8, 10));

        Label statusLabel = new Label(result.getStatusEmoji());
        statusLabel.setMinWidth(50);

        VBox actionBox = new VBox();
        actionBox.setMinWidth(80);
        actionBox.setAlignment(Pos.CENTER);

        if (!result.passed() && result.errorMessage() == null) {
            Button diffButton = new Button("Diff");
            diffButton.getStyleClass().add("small-button");
            diffButton.setOnAction(e -> showDiffDialog(result));
            actionBox.getChildren().add(diffButton);
        }

        Label inputLabel = new Label(result.testCase().input());
        inputLabel.setWrapText(true);
        inputLabel.setPrefWidth(1);
        inputLabel.setMaxWidth(Double.MAX_VALUE);
        inputLabel.setAlignment(Pos.TOP_LEFT);
        inputLabel.setTextAlignment(javafx.scene.text.TextAlignment.LEFT);
        inputLabel.setStyle("-fx-alignment: top-left;");
        HBox.setHgrow(inputLabel, Priority.ALWAYS);

        Label expectedLabel = new Label(result.testCase().expectedOutput());
        expectedLabel.setWrapText(true);
        expectedLabel.setPrefWidth(1);
        expectedLabel.setMaxWidth(Double.MAX_VALUE);
        expectedLabel.setAlignment(Pos.TOP_LEFT);
        expectedLabel.setTextAlignment(javafx.scene.text.TextAlignment.LEFT);
        expectedLabel.setStyle("-fx-alignment: top-left;");
        HBox.setHgrow(expectedLabel, Priority.ALWAYS);

        String actualText = result.errorMessage() != null ? "Error: " + result.errorMessage() : result.actualOutput();
        Label gotLabel = new Label(actualText);
        gotLabel.setWrapText(true);
        gotLabel.setPrefWidth(1);
        gotLabel.setMaxWidth(Double.MAX_VALUE);
        gotLabel.setAlignment(Pos.TOP_LEFT);
        gotLabel.setTextAlignment(javafx.scene.text.TextAlignment.LEFT);
        gotLabel.setStyle("-fx-alignment: top-left;");
        gotLabel.getStyleClass().add(result.passed() ? "text-success" : "text-failure");
        HBox.setHgrow(gotLabel, Priority.ALWAYS);

        row.getChildren().addAll(statusLabel, actionBox, inputLabel, expectedLabel, gotLabel);
        return row;
    }

    private void showDiffDialog(TestResult result) {
        Stage startStage = (Stage) getScene().getWindow();
        Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(startStage);
        dialog.setTitle("Difference Check");
        AppDialogs.applyAppIcon(dialog, getClass());

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("diff-dialog");

        // Expected Section
        VBox expectedBox = new VBox(5);
        expectedBox.getChildren().add(new Label("Expected Output:"));
        TextArea expectedArea = new TextArea(result.testCase().expectedOutput());
        expectedArea.setEditable(false);
        expectedArea.getStyleClass().add("diff-text-area");
        expectedBox.getChildren().add(expectedArea);
        VBox.setVgrow(expectedArea, Priority.ALWAYS);

        // Actual Section
        VBox actualBox = new VBox(5);
        actualBox.getChildren().add(new Label("Your Output:"));
        TextArea actualArea = new TextArea(result.actualOutput());
        actualArea.setEditable(false);
        actualArea.getStyleClass().add("diff-text-area");
        actualBox.getChildren().add(actualArea);
        VBox.setVgrow(actualArea, Priority.ALWAYS);

        // Side-by-side container for Expected and Actual
        HBox compareLayout = new HBox(10);
        compareLayout.getChildren().addAll(expectedBox, actualBox);
        HBox.setHgrow(expectedBox, Priority.ALWAYS);
        HBox.setHgrow(actualBox, Priority.ALWAYS);
        VBox.setVgrow(compareLayout, Priority.ALWAYS);

        // Diff Highlight Section
        VBox diffBox = new VBox(5);
        Label diffLabel = new Label("Comparison (Red = Mismatch):");
        ScrollPane diffScroll = new ScrollPane();
        TextFlow diffFlow = createDiffFlow(result.testCase().expectedOutput(), result.actualOutput());
        diffScroll.setContent(diffFlow);
        diffScroll.setFitToWidth(true);
        VBox.setVgrow(diffScroll, Priority.ALWAYS);
        diffBox.getChildren().addAll(diffLabel, diffScroll);
        VBox.setVgrow(diffBox, Priority.ALWAYS);

        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> dialog.close());
        closeBtn.setMaxWidth(Double.MAX_VALUE);

        root.getChildren().addAll(compareLayout, diffBox, closeBtn);

        Scene scene = new Scene(root, 800, 600);
        if (getScene() != null) {
            scene.getStylesheets().addAll(getScene().getStylesheets());
        }

        dialog.setScene(scene);
        dialog.show();
    }

    private TextFlow createDiffFlow(String expected, String actual) {
        if (expected == null)
            expected = "";
        if (actual == null)
            actual = "";

        TextFlow flow = new TextFlow();
        flow.getStyleClass().add("diff-flow");

        int length = Math.max(expected.length(), actual.length());

        // Simple char-by-char diff
        for (int i = 0; i < length; i++) {
            String charStr;
            boolean isMatch = false;
            boolean isMissing = false;

            if (i < expected.length() && i < actual.length()) {
                char eChar = expected.charAt(i);
                char aChar = actual.charAt(i);
                charStr = String.valueOf(aChar);
                if (eChar == aChar) {
                    isMatch = true;
                }
            } else if (i < actual.length()) {
                // Extra chars in actual
                charStr = String.valueOf(actual.charAt(i));
            } else {
                // Missing chars (expected has more)
                charStr = "[MISSING]"; // Placeholder
                isMissing = true;
            }

            Text text = new Text(charStr);
            text.getStyleClass().add("diff-text");
            if (isMatch) {
                text.getStyleClass().add("diff-match");
            } else if (isMissing) {
                text.setText("[" + expected.charAt(i) + "]");
                text.getStyleClass().add("diff-missing");
            } else {
                text.getStyleClass().add("diff-mismatch");
            }
            flow.getChildren().add(text);
        }

        return flow;
    }

    /**
     * Shows a loading message while tests are running.
     */
    public void showLoading() {
        summaryLabel.setText("⏳ Running tests...");
        summaryLabel.getStyleClass().removeAll("success", "failure");
        resultsContainer.getChildren().clear();
        Label loadingLabel = new Label("Please wait while your code is being compiled and executed.");
        loadingLabel.setPadding(new Insets(10));
        resultsContainer.getChildren().add(loadingLabel);
    }

    /**
     * Clears the results display.
     */
    public void clear() {
        summaryLabel.setText("Run your code to see results");
        summaryLabel.getStyleClass().removeAll("success", "failure");
        resultsContainer.getChildren().clear();
    }

    /**
     * Shows an error message.
     */
    public void showError(String message) {
        summaryLabel.setText("❌ Error");
        summaryLabel.getStyleClass().removeAll("success", "failure");
        summaryLabel.getStyleClass().add("failure");
        resultsContainer.getChildren().clear();
        Label msg = new Label(message);
        msg.setPadding(new Insets(10));
        msg.setWrapText(true);
        resultsContainer.getChildren().add(msg);
    }

    /**
     * Shows a hint for the current question.
     */
    public void showHint(String hint) {
        summaryLabel.setText("💡 Hint");
        summaryLabel.getStyleClass().removeAll("success", "failure");
        resultsContainer.getChildren().clear();

        Label hintLabel = new Label(hint != null && !hint.isEmpty() ? hint : "No hint available.");
        hintLabel.getStyleClass().add("hint-text");
        hintLabel.setWrapText(true);
        hintLabel.setPadding(new Insets(10));

        resultsContainer.getChildren().add(hintLabel);
    }
}
