package dev.philixtheexplorer.buggym.service;

import dev.philixtheexplorer.buggym.application.CodeExecutionEngine;
import dev.philixtheexplorer.buggym.model.RunResult;
import dev.philixtheexplorer.buggym.model.TestCase;

import java.util.List;

/**
 * Current in-process implementation backed by CodeRunner.
 */
public class InProcessCodeExecutionEngine implements CodeExecutionEngine {

    private final CodeRunner codeRunner;

    public InProcessCodeExecutionEngine() {
        this.codeRunner = new CodeRunner();
    }

    @Override
    public RunResult runMain(String code) {
        return codeRunner.runMain(code);
    }

    @Override
    public RunResult runTests(String code, List<TestCase> testCases) {
        return codeRunner.runTests(code, testCases);
    }

    @Override
    public void shutdown() {
        codeRunner.shutdown();
    }
}
