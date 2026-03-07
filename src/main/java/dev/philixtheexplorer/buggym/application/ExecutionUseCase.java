package dev.philixtheexplorer.buggym.application;

import dev.philixtheexplorer.buggym.model.Question;
import dev.philixtheexplorer.buggym.model.RunResult;
import dev.philixtheexplorer.buggym.service.CodeRunner;
import javafx.concurrent.Task;

/**
 * Application use case for executing user code.
 */
public class ExecutionUseCase {

    private final CodeRunner codeRunner;

    public ExecutionUseCase(CodeRunner codeRunner) {
        this.codeRunner = codeRunner;
    }

    public Task<RunResult> createExecutionTask(Question question, String code) {
        if (question.getTestCases().isEmpty()) {
            return new Task<>() {
                @Override
                protected RunResult call() {
                    return codeRunner.runMain(code);
                }
            };
        }

        return new Task<>() {
            @Override
            protected RunResult call() {
                return codeRunner.runTests(code, question.getTestCases());
            }
        };
    }
}
