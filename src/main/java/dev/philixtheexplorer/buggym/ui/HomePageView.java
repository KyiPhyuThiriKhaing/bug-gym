package dev.philixtheexplorer.buggym.ui;

import dev.philixtheexplorer.buggym.model.Category;
import dev.philixtheexplorer.buggym.model.Question;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Home page view with category cards and tags.
 */
public class HomePageView extends ScrollPane {

    private final TilePane cardsPane;
    private Consumer<Category> onCategorySelected;
    private Consumer<Category> onCategoryResetRequested;

    public HomePageView(
            Collection<Category> categories,
            Consumer<Category> onCategorySelected,
            Consumer<Category> onCategoryResetRequested) {
        this.onCategorySelected = onCategorySelected;
        this.onCategoryResetRequested = onCategoryResetRequested;

        VBox homePage = new VBox(16);
        homePage.getStyleClass().add("home-page");
        homePage.setPadding(new Insets(20));
        homePage.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("🏠 Welcome to Bug Gym");
        title.getStyleClass().add("home-title");
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);

        Label subtitle = new Label("Choose a category to start practicing. Focus on one concept at a time.");
        subtitle.getStyleClass().add("home-subtitle");
        subtitle.setWrapText(true);
        subtitle.setMaxWidth(Double.MAX_VALUE);
        subtitle.setAlignment(Pos.CENTER);

        cardsPane = new TilePane();
        cardsPane.getStyleClass().add("home-cards");
        cardsPane.setHgap(12);
        cardsPane.setVgap(12);
        cardsPane.setPrefColumns(3);
        cardsPane.setAlignment(Pos.TOP_CENTER);

        homePage.getChildren().addAll(title, subtitle, cardsPane);
        VBox.setVgrow(cardsPane, Priority.ALWAYS);

        setContent(homePage);
        setFitToWidth(true);
        getStyleClass().add("home-scroll");

        refreshCategories(categories);
    }

    public void setOnCategorySelected(Consumer<Category> onCategorySelected) {
        this.onCategorySelected = onCategorySelected;
    }

    public void setOnCategoryResetRequested(Consumer<Category> onCategoryResetRequested) {
        this.onCategoryResetRequested = onCategoryResetRequested;
    }

    public void refreshCategories(Collection<Category> categories) {
        cardsPane.getChildren().clear();

        for (Category category : categories) {
            if (category.getQuestions().isEmpty()) {
                continue;
            }
            cardsPane.getChildren().add(createCategoryCard(category));
        }
    }

    private HBox createCategoryCard(Category category) {
        long solved = category.getQuestions().stream().filter(Question::isSolved).count();
        int total = category.getQuestions().size();
        double progress = total == 0 ? 0 : (double) solved / total;

        HBox card = new HBox(12);
        card.getStyleClass().add("home-card");
        card.setPadding(new Insets(12));
        card.setAlignment(Pos.CENTER_LEFT);

        VBox leftColumn = new VBox(8);
        leftColumn.getStyleClass().add("home-card-left");
        HBox.setHgrow(leftColumn, Priority.ALWAYS);

        VBox titleMetaBox = new VBox(4);
        Label nameLabel = new Label(category.getDisplayName());
        nameLabel.getStyleClass().add("home-card-title");
        nameLabel.setWrapText(true);

        titleMetaBox.getChildren().add(nameLabel);

        FlowPane tagsPane = createTagsPane(inferTags(category));

        Button startButton = new Button("Start");
        startButton.getStyleClass().add("action-button");
        startButton.getStyleClass().add("home-card-start");
        startButton.setOnAction(e -> {
            selectCategory(category);
            e.consume();
        });

        Button resetButton = new Button("Reset");
        resetButton.getStyleClass().add("small-button");
        resetButton.getStyleClass().add("home-card-reset");
        resetButton.setOnAction(e -> {
            requestCategoryReset(category);
            e.consume();
        });

        HBox actionsRow = new HBox(8, startButton, resetButton);
        actionsRow.setAlignment(Pos.CENTER_LEFT);

        leftColumn.getChildren().addAll(titleMetaBox, tagsPane, actionsRow);

        StackPane progressRing = createProgressRing(solved, total, progress);

        card.getChildren().addAll(leftColumn, progressRing);
        return card;
    }

    private StackPane createProgressRing(long solved, int total, double progress) {
        double radius = 26;
        double circumference = 2 * Math.PI * radius;

        Circle track = new Circle(radius);
        track.getStyleClass().add("home-card-progress-track");

        Circle progressCircle = new Circle(radius);
        progressCircle.getStyleClass().add("home-card-progress-fill");
        progressCircle.getStrokeDashArray().setAll(circumference, circumference);
        progressCircle.setStrokeDashOffset(circumference * (1 - progress));
        progressCircle.setRotate(-90);

        Label ratioLabel = new Label("%d/%d".formatted(solved, total));
        ratioLabel.getStyleClass().add("home-card-progress-text");

        StackPane ring = new StackPane(track, progressCircle, ratioLabel);
        ring.getStyleClass().add("home-card-progress");
        ring.setMinSize(64, 64);
        ring.setPrefSize(64, 64);
        ring.setMaxSize(64, 64);
        return ring;
    }

    private void selectCategory(Category category) {
        if (onCategorySelected != null) {
            onCategorySelected.accept(category);
        }
    }

    private void requestCategoryReset(Category category) {
        if (onCategoryResetRequested != null) {
            onCategoryResetRequested.accept(category);
        }
    }

    private FlowPane createTagsPane(List<String> tags) {
        FlowPane tagsPane = new FlowPane();
        tagsPane.setHgap(6);
        tagsPane.setVgap(6);
        tagsPane.getStyleClass().add("home-tags");

        for (String tag : tags) {
            Label tagLabel = new Label(tag);
            tagLabel.getStyleClass().add("home-tag");
            tagsPane.getChildren().add(tagLabel);
        }

        return tagsPane;
    }

    private List<String> inferTags(Category category) {
        String name = category.getName().toLowerCase(Locale.ROOT);
        LinkedHashSet<String> tags = new LinkedHashSet<>();

        if (name.contains("practice"))
            tags.add("Practice");
        if (name.contains("lab"))
            tags.add("Lab");
        if (name.contains("quiz"))
            tags.add("Quiz");
        if (name.contains("dod"))
            tags.add("Exam");
        if (name.contains("basic"))
            tags.add("Basics");
        if (name.contains("condition"))
            tags.add("Conditionals");
        if (name.contains("loop"))
            tags.add("Loops");
        if (name.contains("string"))
            tags.add("Strings");
        if (name.contains("array"))
            tags.add("Arrays");
        if (name.contains("method"))
            tags.add("Methods");
        if (name.contains("mock"))
            tags.add("Mock");

        if (tags.isEmpty()) {
            tags.add("General");
        }

        return tags.stream().limit(3).collect(Collectors.toList());
    }
}
