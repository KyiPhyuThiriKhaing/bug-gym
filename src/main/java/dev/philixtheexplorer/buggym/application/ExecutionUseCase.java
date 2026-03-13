package dev.philixtheexplorer.buggym.application;

import dev.philixtheexplorer.buggym.model.Question;
import dev.philixtheexplorer.buggym.model.RunResult;
import javafx.concurrent.Task;

/**
 * Application use case for executing user code.
 */
public class ExecutionUseCase {

    private final CodeExecutionEngine codeExecutionEngine;

    public ExecutionUseCase(CodeExecutionEngine codeExecutionEngine) {
        this.codeExecutionEngine = codeExecutionEngine;
    }

    public Task<RunResult> createExecutionTask(Question question, String code) {
        if (question.getTestCases().isEmpty()) {
            return new Task<>() {
                @Override
                protected RunResult call() {
                    return codeExecutionEngine.runMain(code);
                }
            };
        }

        return new Task<>() {
            @Override
            protected RunResult call() {
                return codeExecutionEngine.runTests(code, question.getTestCases());
            }
        };
    }
}
