package dev.philixtheexplorer.buggym;

import dev.philixtheexplorer.buggym.application.AppController;
import dev.philixtheexplorer.buggym.model.Category;
import dev.philixtheexplorer.buggym.model.Question;
import dev.philixtheexplorer.buggym.model.RunResult;
import dev.philixtheexplorer.buggym.service.CodeRunner;
import dev.philixtheexplorer.buggym.service.ProgressManager;
import dev.philixtheexplorer.buggym.service.QuestionLoader;
import dev.philixtheexplorer.buggym.service.UpdateService;
import dev.philixtheexplorer.buggym.ui.AppDialogs;
import dev.philixtheexplorer.buggym.ui.CodeEditor;
import dev.philixtheexplorer.buggym.ui.HomePageView;
import dev.philixtheexplorer.buggym.ui.MainMenuBarFactory;
import dev.philixtheexplorer.buggym.ui.MainWorkspacePane;
import dev.philixtheexplorer.buggym.ui.QuestionTreeView;
import dev.philixtheexplorer.buggym.ui.ResultsPanel;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * Bug Gym - A mini coding practice platform for Java beginners.
 */
public class App extends Application {

    private static final double WINDOW_WIDTH = 1000;
    private static final double WINDOW_HEIGHT = 600;

    private AppController appController;
    private CodeRunner codeRunner;
    private UpdateService updateService;

    private QuestionTreeView questionTree;
    private WebView questionView;
    private CodeEditor codeEditor;
    private ResultsPanel resultsPanel;
    private Label progressLabel;

    private boolean darkMode = true;
    private boolean suppressPracticeAutoSwitch = false;

    private MainWorkspacePane workspacePane;
    private SplitPane mainContentSplit;
    private StackPane contentStack;
    private HomePageView homeContainer;

    @Override
    public void start(Stage stage) {
        Font.loadFont(getClass().getResourceAsStream("/fonts/JetBrainsMono-Regular.ttf"), 14);

        QuestionLoader questionLoader = new QuestionLoader();
        ProgressManager progressManager = new ProgressManager();
        appController = new AppController(questionLoader, progressManager);

        codeRunner = new CodeRunner();
        updateService = new UpdateService();

        try {
            appController.loadQuestionsAndProgress();
        } catch (IOException e) {
            showError("Failed to load questions", e.getMessage());
        }

        BorderPane root = createMainLayout();

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());

        stage.setTitle("Bug Gym - Java Practice Platform");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/bug-gym.png")));
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setMaximized(true);

        stage.setOnCloseRequest(event -> codeRunner.shutdown());

        stage.show();

        selectFirstQuestion();
        checkForUpdates(true);
    }

    private BorderPane createMainLayout() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");

        MenuBar menuBar = MainMenuBarFactory.create(appController.getCategories(), darkMode,
                new MainMenuBarFactory.Actions(
                        this::saveProgress,
                        Platform::exit,
                        this::clearCode,
                        this::resetToStarter,
                        this::toggleDarkMode,
                        this::showHomePage,
                        this::showPracticePage,
                        this::toggleSidebar,
                        this::increaseZoom,
                        this::decreaseZoom,
                        this::resetZoom,
                        this::runTests,
                        this::submitSolution,
                        this::openCategoryFromMenu,
                        this::showHint,
                        this::showKeyboardShortcuts,
                        () -> checkForUpdates(false),
                        this::showAbout));
        root.setTop(menuBar);

        workspacePane = new MainWorkspacePane(
                appController.getCategories(),
                this::onQuestionSelected,
                this::navigateQuestion,
                this::runTests,
                this::submitSolution,
                this::clearCode,
                this::resetToStarter,
                this::showHint);

        questionTree = workspacePane.getQuestionTree();
        codeEditor = workspacePane.getCodeEditor();
        questionView = workspacePane.getQuestionView();
        resultsPanel = workspacePane.getResultsPanel();
        progressLabel = workspacePane.getProgressLabel();

        mainContentSplit = workspacePane;
        updateProgress();

        homeContainer = new HomePageView(appController.getCategories(), this::openCategoryFromHome);

        contentStack = new StackPane();
        contentStack.getChildren().addAll(mainContentSplit, homeContainer);
        root.setCenter(contentStack);

        showHomePage();
        return root;
    }

    private void openCategoryFromMenu(Category category) {
        if (!category.getQuestions().isEmpty()) {
            questionTree.selectQuestion(category.getQuestions().get(0));
        }
    }

    private void updateProgress() {
        if (progressLabel == null || appController == null) {
            return;
        }

        AppController.ProgressSnapshot snapshot = appController.getProgressSnapshot();
        progressLabel.setText("Progress: %d/%d solved"
                .formatted(snapshot.solvedQuestions(), snapshot.totalQuestions()));

        if (homeContainer != null) {
            homeContainer.refreshCategories(appController.getCategories());
        }
    }

    private void openCategoryFromHome(Category category) {
        Question target = appController.getFirstUnsolvedOrFirst(category);
        showPracticePage();
        questionTree.selectQuestion(target);
    }

    private void showHomePage() {
        if (homeContainer == null || mainContentSplit == null) {
            return;
        }

        homeContainer.refreshCategories(appController.getCategories());
        homeContainer.setVisible(true);
        homeContainer.setManaged(true);
        mainContentSplit.setVisible(false);
        mainContentSplit.setManaged(false);
    }

    private void showPracticePage() {
        if (homeContainer == null || mainContentSplit == null) {
            return;
        }

        homeContainer.setVisible(false);
        homeContainer.setManaged(false);
        mainContentSplit.setVisible(true);
        mainContentSplit.setManaged(true);
    }

    private void increaseZoom() {
        if (codeEditor != null) {
            codeEditor.increaseFontSize();
        }
    }

    private void decreaseZoom() {
        if (codeEditor != null) {
            codeEditor.decreaseFontSize();
        }
    }

    private void resetZoom() {
        if (codeEditor != null) {
            codeEditor.resetFontSize();
        }
    }

    private void onQuestionSelected(Question question) {
        if (question == null) {
            return;
        }

        appController.persistCurrentCode(codeEditor.getCode());
        appController.setCurrentQuestion(question);

        if (!suppressPracticeAutoSwitch) {
            showPracticePage();
        }

        String html = appController.getQuestionHtml(question, darkMode);
        questionView.getEngine().loadContent(html);

        codeEditor.setCode(appController.getInitialCodeFor(question));
        resultsPanel.clear();
    }

    private void navigateQuestion(int direction) {
        Question currentQuestion = appController.getCurrentQuestion();
        if (currentQuestion == null) {
            selectFirstQuestion();
            return;
        }

        var questions = appController.getQuestions();
        int idx = questions.indexOf(currentQuestion);
        int newIdx = idx + direction;
        if (newIdx >= 0 && newIdx < questions.size()) {
            questionTree.selectQuestion(questions.get(newIdx));
        }
    }

    private void executeCodeRun(boolean isSubmission) {
        Question currentQuestion = appController.getCurrentQuestion();
        if (currentQuestion == null) {
            showError("No Question Selected", "Please select a question from the sidebar.");
            return;
        }

        String code = codeEditor.getCode();
        if (code.trim().isEmpty()) {
            showError("Empty Code", "Please write some code before running tests.");
            return;
        }

        if (currentQuestion.getTestCases().isEmpty()) {
            Task<RunResult> task = new Task<>() {
                @Override
                protected RunResult call() {
                    return codeRunner.runMain(code);
                }
            };

            resultsPanel.showLoading();
            codeEditor.setEditable(false);

            task.setOnSucceeded(e -> {
                RunResult result = task.getValue();
                resultsPanel.showResults(result);
                codeEditor.setEditable(true);
            });

            task.setOnFailed(e -> {
                resultsPanel.showError("Execution failed: " + task.getException().getMessage());
                codeEditor.setEditable(true);
            });

            new Thread(task).start();
            return;
        }

        resultsPanel.showLoading();
        codeEditor.setEditable(false);

        Task<RunResult> task = new Task<>() {
            @Override
            protected RunResult call() {
                return codeRunner.runTests(code, currentQuestion.getTestCases());
            }
        };

        task.setOnSucceeded(e -> {
            RunResult result = task.getValue();
            resultsPanel.showResults(result);
            codeEditor.setEditable(true);

            if (isSubmission && result.allPassed()) {
                handleSuccessfulSubmission();
            }
        });

        task.setOnFailed(e -> {
            resultsPanel.showError("Execution failed: " + task.getException().getMessage());
            codeEditor.setEditable(true);
        });

        new Thread(task).start();
    }

    private void handleSuccessfulSubmission() {
        appController.markCurrentSolved(codeEditor.getCode());

        Question currentQuestion = appController.getCurrentQuestion();
        questionTree.refreshQuestion(currentQuestion);
        updateProgress();

        Alert alert = AppDialogs.createStyledAlert(getClass(), Alert.AlertType.INFORMATION,
                "Congratulations!", "🎉 All tests passed!");
        alert.setContentText("Great job! You've successfully solved this question.");

        ButtonType nextQuestionBtn = new ButtonType("Next Question", ButtonBar.ButtonData.NEXT_FORWARD);
        ButtonType stayBtn = new ButtonType("Stay Here", ButtonBar.ButtonData.CANCEL_CLOSE);

        Question nextQuestion = appController.getNextQuestion();
        if (nextQuestion != null) {
            alert.getButtonTypes().setAll(nextQuestionBtn, stayBtn);
        } else {
            alert.getButtonTypes().setAll(ButtonType.OK);
        }

        Optional<ButtonType> resultBtn = alert.showAndWait();
        if (resultBtn.isPresent() && resultBtn.get() == nextQuestionBtn) {
            questionTree.selectQuestion(nextQuestion);
        }
    }

    private void runTests() {
        executeCodeRun(false);
    }

    private void submitSolution() {
        executeCodeRun(true);
    }

    private void clearCode() {
        codeEditor.clear();
        resultsPanel.clear();
    }

    private void resetToStarter() {
        Question currentQuestion = appController.getCurrentQuestion();
        if (currentQuestion != null) {
            codeEditor.setCode(currentQuestion.getStarterCode());
            resultsPanel.clear();
        }
    }

    private void showHint() {
        Question currentQuestion = appController.getCurrentQuestion();
        if (currentQuestion != null) {
            resultsPanel.showHint(currentQuestion.getHint());
        }
    }

    private void toggleDarkMode(boolean dark) {
        this.darkMode = dark;

        Scene scene = codeEditor.getScene();
        if (scene != null) {
            if (dark) {
                scene.getRoot().getStyleClass().remove("light-mode");
            } else {
                scene.getRoot().getStyleClass().add("light-mode");
            }
        }

        Question currentQuestion = appController.getCurrentQuestion();
        if (currentQuestion != null) {
            String html = appController.getQuestionHtml(currentQuestion, darkMode);
            questionView.getEngine().loadContent(html);
        }
    }

    private void selectFirstQuestion() {
        Question first = appController.getFirstQuestion();
        suppressPracticeAutoSwitch = true;
        if (first != null) {
            questionTree.selectQuestion(first);
        }
        suppressPracticeAutoSwitch = false;
    }

    private void showError(String title, String message) {
        AppDialogs.showError(getClass(), title, message);
    }

    private void showAbout() {
        String version = getAppVersion();
        AppDialogs.showAbout(getClass(), version, url -> getHostServices().showDocument(url));
    }

    private String getAppVersion() {
        String fromManifest = App.class.getPackage() != null
                ? App.class.getPackage().getImplementationVersion()
                : null;
        if (fromManifest != null && !fromManifest.isBlank()) {
            return fromManifest;
        }

        String fromProperty = System.getProperty("app.version");
        if (fromProperty != null && !fromProperty.isBlank()) {
            return fromProperty;
        }

        return "dev";
    }

    private void checkForUpdates(boolean silent) {
        Task<String> task = new Task<>() {
            @Override
            protected String call() throws Exception {
                return updateService.fetchLatestVersion();
            }
        };

        task.setOnSucceeded(e -> {
            String latest = task.getValue();
            String current = getAppVersion();
            if (latest == null) {
                if (!silent) {
                    showInfo("Update Check", "Could not retrieve version info.");
                }
            } else if ("dev".equals(current) || updateService.compareVersions(current, latest) < 0) {
                AppDialogs.showUpdateAvailable(getClass(), current, latest,
                        url -> getHostServices().showDocument(url));
            } else {
                if (!silent) {
                    showInfo("Up to Date", "You are running the latest version (v" + current + ").");
                }
            }
        });

        task.setOnFailed(e -> {
            if (!silent) {
                showInfo("Update Check", "Could not check for updates.");
            }
        });

        new Thread(task).start();
    }

    private void showInfo(String title, String message) {
        AppDialogs.showInfo(getClass(), title, message);
    }

    private void showKeyboardShortcuts() {
        AppDialogs.showKeyboardShortcuts(getClass());
    }

    private void saveProgress() {
        appController.persistCurrentCode(codeEditor.getCode());
    }

    private void toggleSidebar() {
        if (workspacePane == null) {
            return;
        }
        workspacePane.toggleSidebar();
    }

    @Override
    public void stop() {
        if (codeEditor != null && appController != null) {
            appController.persistCurrentCode(codeEditor.getCode());
        }
        if (codeRunner != null) {
            codeRunner.shutdown();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
