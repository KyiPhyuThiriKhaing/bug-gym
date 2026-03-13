package dev.philixtheexplorer.buggym;

import dev.philixtheexplorer.buggym.application.AppController;
import dev.philixtheexplorer.buggym.application.BackgroundTaskRunner;
import dev.philixtheexplorer.buggym.application.CodeExecutionEngine;
import dev.philixtheexplorer.buggym.application.ExecutionUseCase;
import dev.philixtheexplorer.buggym.application.SessionFlowCoordinator;
import dev.philixtheexplorer.buggym.application.UpdateCheckUseCase;
import dev.philixtheexplorer.buggym.service.InProcessCodeExecutionEngine;
import dev.philixtheexplorer.buggym.service.ProgressManager;
import dev.philixtheexplorer.buggym.service.QuestionLoader;
import dev.philixtheexplorer.buggym.service.UpdateService;
import dev.philixtheexplorer.buggym.ui.StageConfigurator;
import dev.philixtheexplorer.buggym.ui.SubmissionFeedbackCoordinator;
import dev.philixtheexplorer.buggym.ui.UpdateFeedbackCoordinator;
import dev.philixtheexplorer.buggym.ui.WorkspaceUiCoordinator;

import java.io.IOException;

/**
 * Composes application dependencies and startup state.
 */
public class AppBootstrap {

    public BootstrapContext initialize() throws IOException {
        QuestionLoader questionLoader = new QuestionLoader();
        ProgressManager progressManager = new ProgressManager();
        AppController appController = new AppController(questionLoader, progressManager);
        BackgroundTaskRunner taskRunner = new BackgroundTaskRunner("buggym-app-bg", 2);

        CodeExecutionEngine codeExecutionEngine = new InProcessCodeExecutionEngine();
        ExecutionUseCase executionUseCase = new ExecutionUseCase(codeExecutionEngine);
        SessionFlowCoordinator sessionFlowCoordinator = new SessionFlowCoordinator(appController);

        UpdateService updateService = new UpdateService();
        UpdateCheckUseCase updateCheckUseCase = new UpdateCheckUseCase(updateService);

        WorkspaceUiCoordinator workspaceUiCoordinator = new WorkspaceUiCoordinator();
        StageConfigurator stageConfigurator = new StageConfigurator();
        SubmissionFeedbackCoordinator submissionFeedbackCoordinator = new SubmissionFeedbackCoordinator();
        UpdateFeedbackCoordinator updateFeedbackCoordinator = new UpdateFeedbackCoordinator();

        appController.loadQuestionsAndProgress();

        return new BootstrapContext(
                appController,
                taskRunner,
                codeExecutionEngine,
                executionUseCase,
                sessionFlowCoordinator,
                updateCheckUseCase,
                workspaceUiCoordinator,
                stageConfigurator,
                submissionFeedbackCoordinator,
                updateFeedbackCoordinator);
    }

    public record BootstrapContext(
            AppController appController,
            BackgroundTaskRunner taskRunner,
            CodeExecutionEngine codeExecutionEngine,
            ExecutionUseCase executionUseCase,
            SessionFlowCoordinator sessionFlowCoordinator,
            UpdateCheckUseCase updateCheckUseCase,
            WorkspaceUiCoordinator workspaceUiCoordinator,
            StageConfigurator stageConfigurator,
            SubmissionFeedbackCoordinator submissionFeedbackCoordinator,
            UpdateFeedbackCoordinator updateFeedbackCoordinator
    ) {
        public void shutdownRuntime() {
            taskRunner.shutdownNow();
            codeExecutionEngine.shutdown();
        }
    }
}
