package dev.philixtheexplorer.buggym.ui;

import dev.philixtheexplorer.buggym.model.Category;
import dev.philixtheexplorer.buggym.model.Question;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

import java.util.Collection;
import java.util.function.Consumer;

public class MainWorkspacePane extends SplitPane {

    private static final double SIDEBAR_WIDTH = 180;

    private final VBox sidebarContainer;
    private final QuestionTreeView questionTree;
    private final Label progressLabel;

    private CodeEditor codeEditor;
    private WebView questionView;
    private ResultsPanel resultsPanel;

    private double lastSidebarDividerPosition = 0.1;
    private boolean sidebarVisible = true;

    public MainWorkspacePane(
            Collection<Category> categories,
            Consumer<Question> onQuestionSelected,
            Consumer<Integer> onNavigateQuestion,
            Runnable onRunTests,
            Runnable onSubmit,
            Runnable onClear,
            Runnable onReset,
            Runnable onHint
    ) {
        setOrientation(Orientation.HORIZONTAL);

        sidebarContainer = new VBox(10);
        sidebarContainer.setPrefWidth(SIDEBAR_WIDTH);
        sidebarContainer.setMinWidth(150);
        sidebarContainer.setPadding(new Insets(10));
        sidebarContainer.getStyleClass().add("sidebar");

        Label sidebarTitle = new Label("📚 Questions");
        sidebarTitle.getStyleClass().add("sidebar-title");

        questionTree = new QuestionTreeView();
        questionTree.setCategories(categories);
        questionTree.setOnQuestionSelected(onQuestionSelected);
        VBox.setVgrow(questionTree, Priority.ALWAYS);

        progressLabel = new Label();
        progressLabel.getStyleClass().add("progress-label");

        sidebarContainer.getChildren().addAll(sidebarTitle, questionTree, progressLabel);

        SplitPane centerArea = createCenterArea(onNavigateQuestion, onRunTests, onSubmit, onClear, onReset, onHint);

        getItems().addAll(sidebarContainer, centerArea);
        setDividerPositions(lastSidebarDividerPosition);
    }

    private SplitPane createCenterArea(
            Consumer<Integer> onNavigateQuestion,
            Runnable onRunTests,
            Runnable onSubmit,
            Runnable onClear,
            Runnable onReset,
            Runnable onHint
    ) {
        SplitPane mainSplit = new SplitPane();
        mainSplit.setOrientation(Orientation.HORIZONTAL);
        mainSplit.setDividerPositions(0.5);

        VBox editorColumn = new VBox(10);
        editorColumn.setPadding(new Insets(10));
        editorColumn.getStyleClass().add("code-container");

        Label editorLabel = new Label("💻 Your Solution:");
        editorLabel.getStyleClass().add("section-label");

        codeEditor = new CodeEditor();
        VBox.setVgrow(codeEditor, Priority.ALWAYS);

        HBox buttonBar = createButtonBar(onRunTests, onSubmit, onClear, onReset, onHint);
        editorColumn.getChildren().addAll(editorLabel, codeEditor, buttonBar);

        SplitPane rightColumn = new SplitPane();
        rightColumn.setOrientation(Orientation.VERTICAL);
        rightColumn.setDividerPositions(0.5);

        questionView = new WebView();
        questionView.getStyleClass().add("question-view");

        Button prevBtn = new Button("← Prev");
        prevBtn.getStyleClass().add("action-button");
        prevBtn.setOnAction(e -> onNavigateQuestion.accept(-1));

        Label questionNavLabel = new Label("📖 Question");
        questionNavLabel.getStyleClass().add("section-label");

        Button nextBtn = new Button("Next →");
        nextBtn.getStyleClass().add("action-button");
        nextBtn.setOnAction(e -> onNavigateQuestion.accept(1));

        Region navSpacer1 = new Region();
        Region navSpacer2 = new Region();
        HBox.setHgrow(navSpacer1, Priority.ALWAYS);
        HBox.setHgrow(navSpacer2, Priority.ALWAYS);

        HBox navBar = new HBox(10, prevBtn, navSpacer1, questionNavLabel, navSpacer2, nextBtn);
        navBar.setAlignment(Pos.CENTER);
        navBar.setPadding(new Insets(5, 10, 5, 10));

        VBox questionContainer = new VBox(navBar, questionView);
        questionContainer.getStyleClass().add("question-container");
        VBox.setVgrow(questionView, Priority.ALWAYS);

        Label resultsLabel = new Label("📊 Results:");
        resultsLabel.getStyleClass().add("section-label");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button toggleBtn = new Button("▼");
        toggleBtn.getStyleClass().add("small-button");
        toggleBtn.setTooltip(new Tooltip("Minimize"));

        HBox resultsHeader = new HBox(10, resultsLabel, spacer, toggleBtn);
        resultsHeader.setAlignment(Pos.CENTER_LEFT);

        resultsPanel = new ResultsPanel();
        VBox.setVgrow(resultsPanel, Priority.ALWAYS);

        VBox resultsBox = new VBox(5, resultsHeader, resultsPanel);
        resultsBox.setPadding(new Insets(10));
        VBox.setVgrow(resultsPanel, Priority.ALWAYS);
        resultsBox.getStyleClass().add("results-box");

        rightColumn.getItems().addAll(questionContainer, resultsBox);

        final double[] lastDividerPosition = {0.5};
        toggleBtn.setOnAction(e -> {
            boolean isExpanded = resultsPanel.isVisible();
            if (isExpanded) {
                if (rightColumn.getDividerPositions().length > 0) {
                    lastDividerPosition[0] = rightColumn.getDividerPositions()[0];
                }
                resultsPanel.setVisible(false);
                resultsPanel.setManaged(false);
                toggleBtn.setText("▲");
                toggleBtn.setTooltip(new Tooltip("Expand"));
                rightColumn.setDividerPositions(1.0);
            } else {
                resultsPanel.setVisible(true);
                resultsPanel.setManaged(true);
                toggleBtn.setText("▼");
                toggleBtn.setTooltip(new Tooltip("Minimize"));
                rightColumn.setDividerPositions(lastDividerPosition[0]);
            }
        });

        mainSplit.getItems().addAll(editorColumn, rightColumn);
        return mainSplit;
    }

    private HBox createButtonBar(
            Runnable onRunTests,
            Runnable onSubmit,
            Runnable onClear,
            Runnable onReset,
            Runnable onHint
    ) {
        HBox buttonBar = new HBox(10);
        buttonBar.setPadding(new Insets(10, 0, 0, 0));
        buttonBar.getStyleClass().add("button-bar");

        Button runButton = new Button("Run Tests");
        runButton.getStyleClass().addAll("action-button", "run-button");
        runButton.setOnAction(e -> onRunTests.run());

        Button submitButton = new Button("Submit");
        submitButton.getStyleClass().addAll("action-button", "submit-button");
        submitButton.setOnAction(e -> onSubmit.run());

        Button clearButton = new Button("Clear");
        clearButton.getStyleClass().add("action-button");
        clearButton.setOnAction(e -> onClear.run());

        Button resetButton = new Button("Reset");
        resetButton.getStyleClass().add("action-button");
        resetButton.setOnAction(e -> onReset.run());

        Button hintButton = new Button("Hint");
        hintButton.getStyleClass().add("action-button");
        hintButton.setOnAction(e -> onHint.run());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        buttonBar.getChildren().addAll(runButton, submitButton, spacer, clearButton, resetButton, hintButton);
        return buttonBar;
    }

    public QuestionTreeView getQuestionTree() {
        return questionTree;
    }

    public CodeEditor getCodeEditor() {
        return codeEditor;
    }

    public WebView getQuestionView() {
        return questionView;
    }

    public ResultsPanel getResultsPanel() {
        return resultsPanel;
    }

    public Label getProgressLabel() {
        return progressLabel;
    }

    public void toggleSidebar() {
        if (sidebarVisible) {
            if (getDividerPositions().length > 0) {
                lastSidebarDividerPosition = getDividerPositions()[0];
            }
            getItems().remove(sidebarContainer);
            sidebarVisible = false;
        } else {
            if (!getItems().contains(sidebarContainer)) {
                getItems().add(0, sidebarContainer);
            }
            sidebarVisible = true;
            Platform.runLater(() -> setDividerPositions(lastSidebarDividerPosition));
        }
    }
}
