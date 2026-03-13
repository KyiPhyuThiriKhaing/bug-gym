package dev.philixtheexplorer.buggym.application;

import dev.philixtheexplorer.buggym.model.RunResult;
import dev.philixtheexplorer.buggym.model.TestCase;

import java.util.List;

/**
 * Abstraction for executing user code.
 */
public interface CodeExecutionEngine {

    RunResult runMain(String code);

    RunResult runTests(String code, List<TestCase> testCases);

    void shutdown();
}
