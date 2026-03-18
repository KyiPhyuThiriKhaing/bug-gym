package dev.philixtheexplorer.buggym.service;

import dev.philixtheexplorer.buggym.model.Question;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ProgressManager {

    private final Path saveDir;
    private final Path statusFile;

    public ProgressManager() {
        this.saveDir = Paths.get(System.getProperty("user.home"), ".buggym", "saves");
        this.statusFile = saveDir.resolve("status.properties");
        try {
            Files.createDirectories(saveDir);
            if (!Files.exists(statusFile)) {
                Files.createFile(statusFile);
            }
        } catch (IOException e) {
            System.err.println("Failed to initialize save directory: " + e.getMessage());
        }
    }

    public void saveProgress(Question question) {
        try {
            // Save solved status
            Properties props = new Properties();
            if (Files.exists(statusFile)) {
                try (var reader = Files.newBufferedReader(statusFile)) {
                    props.load(reader);
                }
            }
            props.setProperty(question.getId(), String.valueOf(question.isSolved()));
            try (var writer = Files.newBufferedWriter(statusFile)) {
                props.store(writer, "BugGym Progress");
            }

            // Save user code
            if (question.getUserCode() != null) {
                Path codeFile = saveDir.resolve(question.getId() + ".java");
                Files.writeString(codeFile, question.getUserCode());
            }

        } catch (IOException e) {
            System.err.println("Failed to save progress: " + e.getMessage());
        }
    }

    public void loadProgress(Question question) {
        try {
            // Load solved status
            if (Files.exists(statusFile)) {
                Properties props = new Properties();
                try (var reader = Files.newBufferedReader(statusFile)) {
                    props.load(reader);
                    String solvedStr = props.getProperty(question.getId());
                    if (solvedStr != null) {
                        question.setSolved(Boolean.parseBoolean(solvedStr));
                    }
                }
            }

            // Load user code
            Path codeFile = saveDir.resolve(question.getId() + ".java");
            if (Files.exists(codeFile)) {
                String code = Files.readString(codeFile);
                question.setUserCode(code);
            }

        } catch (IOException e) {
            System.err.println("Failed to load progress for " + question.getId() + ": " + e.getMessage());
        }
    }

    public void resetProgress(Question question) {
        try {
            Properties props = new Properties();
            if (Files.exists(statusFile)) {
                try (var reader = Files.newBufferedReader(statusFile)) {
                    props.load(reader);
                }
            }

            props.setProperty(question.getId(), "false");
            try (var writer = Files.newBufferedWriter(statusFile)) {
                props.store(writer, "BugGym Progress");
            }

            Path codeFile = saveDir.resolve(question.getId() + ".java");
            Files.deleteIfExists(codeFile);
        } catch (IOException e) {
            System.err.println("Failed to reset progress for " + question.getId() + ": " + e.getMessage());
        }
    }
}
