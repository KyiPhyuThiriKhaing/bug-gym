package dev.philixtheexplorer.buggym.application;

import dev.philixtheexplorer.buggym.model.Category;
import dev.philixtheexplorer.buggym.model.Question;

import java.util.List;
import java.util.function.Consumer;

/**
 * Coordinates session-level question flow and persistence interactions.
 */
public class SessionFlowCoordinator {

    private final AppController appController;

    public SessionFlowCoordinator(AppController appController) {
        this.appController = appController;
    }

    public SelectionUpdate handleQuestionSelected(
            Question question,
            String editorCode,
            boolean suppressPracticeAutoSwitch,
            boolean darkMode) {
        if (question == null) {
            return null;
        }

        appController.persistCurrentCode(editorCode);
        appController.setCurrentQuestion(question);

        return new SelectionUpdate(
                !suppressPracticeAutoSwitch,
                appController.getQuestionHtml(question, darkMode),
                appController.getInitialCodeFor(question));
    }

    public void navigateQuestion(
            int direction,
            Runnable selectFirstQuestion,
            Consumer<Question> selectQuestion) {
        Question currentQuestion = appController.getCurrentQuestion();
        if (currentQuestion == null) {
            selectFirstQuestion.run();
            return;
        }

        List<Question> questions = appController.getQuestions();
        int idx = questions.indexOf(currentQuestion);
        int newIdx = idx + direction;
        if (newIdx >= 0 && newIdx < questions.size()) {
            selectQuestion.accept(questions.get(newIdx));
        }
    }

    public Question resolveHomeTarget(Category category) {
        return appController.getFirstUnsolvedOrFirst(category);
    }

    public Question getFirstQuestion() {
        return appController.getFirstQuestion();
    }

    public void persistCurrentCode(String code) {
        appController.persistCurrentCode(code);
    }

    public record SelectionUpdate(boolean shouldShowPracticePage, String questionHtml, String codeToLoad) {
    }
}
