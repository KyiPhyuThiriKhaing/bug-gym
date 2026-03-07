package dev.philixtheexplorer.buggym.application;

import dev.philixtheexplorer.buggym.model.Category;
import dev.philixtheexplorer.buggym.model.Question;
import dev.philixtheexplorer.buggym.service.ProgressManager;
import dev.philixtheexplorer.buggym.service.QuestionLoader;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Application-layer coordinator for question/session state.
 *
 * Keeps UI classes focused on rendering and user interaction.
 */
public class AppController {

    private final QuestionLoader questionLoader;
    private final ProgressManager progressManager;

    private Question currentQuestion;

    public AppController(QuestionLoader questionLoader, ProgressManager progressManager) {
        this.questionLoader = questionLoader;
        this.progressManager = progressManager;
    }

    public void loadQuestionsAndProgress() throws IOException {
        questionLoader.loadQuestions();
        for (Question question : questionLoader.getQuestions()) {
            progressManager.loadProgress(question);
        }
    }

    public Collection<Category> getCategories() {
        return questionLoader.getCategories();
    }

    public List<Question> getQuestions() {
        return questionLoader.getQuestions();
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(Question question) {
        this.currentQuestion = question;
    }

    public void persistCurrentCode(String currentCode) {
        if (currentQuestion == null) {
            return;
        }
        currentQuestion.setUserCode(currentCode);
        progressManager.saveProgress(currentQuestion);
    }

    public void markCurrentSolved(String currentCode) {
        if (currentQuestion == null) {
            return;
        }
        currentQuestion.setSolved(true);
        currentQuestion.setUserCode(currentCode);
        progressManager.saveProgress(currentQuestion);
    }

    public String getQuestionHtml(Question question, boolean darkMode) {
        return questionLoader.getQuestionHtml(question, darkMode);
    }

    public String getInitialCodeFor(Question question) {
        if (question.getUserCode() != null && !question.getUserCode().isEmpty()) {
            return question.getUserCode();
        }
        return question.getStarterCode();
    }

    public Question getNextQuestion() {
        if (currentQuestion == null) {
            return null;
        }

        List<Question> allQuestions = questionLoader.getQuestions();
        int index = allQuestions.indexOf(currentQuestion);
        if (index >= 0 && index < allQuestions.size() - 1) {
            return allQuestions.get(index + 1);
        }
        return null;
    }

    public Question getFirstQuestion() {
        for (Category category : questionLoader.getCategories()) {
            if (!category.getQuestions().isEmpty()) {
                return category.getQuestions().get(0);
            }
        }
        return null;
    }

    public Question getFirstUnsolvedOrFirst(Category category) {
        return category.getQuestions().stream()
                .filter(q -> !q.isSolved())
                .findFirst()
                .orElse(category.getQuestions().get(0));
    }

    public ProgressSnapshot getProgressSnapshot() {
        long totalQuestions = questionLoader.getQuestions().size();
        long solvedQuestions = questionLoader.getQuestions().stream()
                .filter(Question::isSolved)
                .count();
        return new ProgressSnapshot(solvedQuestions, totalQuestions);
    }

    public record ProgressSnapshot(long solvedQuestions, long totalQuestions) {
    }
}
