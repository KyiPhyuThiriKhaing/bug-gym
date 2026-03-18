package dev.philixtheexplorer.buggym.service;

import dev.philixtheexplorer.buggym.model.Category;
import dev.philixtheexplorer.buggym.model.Question;
import dev.philixtheexplorer.buggym.model.TestCase;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service for loading questions from the resources/questions folder.
 * Automatically discovers category folders and questions within them from the
 * entire directory tree.
 */
public class QuestionLoader {

    private static final String QUESTIONS_PATH = "/questions";

    private final MarkdownParser markdownParser;
    private final Map<String, Category> categories; // LinkedHashMap to preserve insertion order (pre-sort)
    private final List<Question> questions;

    public QuestionLoader() {
        this.markdownParser = new MarkdownParser();
        this.categories = new LinkedHashMap<>();
        this.questions = new ArrayList<>();
    }

    /**
     * Loads all questions from the resources/questions folder recursively.
     */
    public void loadQuestions() throws IOException {
        categories.clear();
        questions.clear();

        loadAllQuestionsRecursively();

        // Sort questions within each category by order
        for (Category category : categories.values()) {
            category.getQuestions().sort(Comparator.comparingInt(Question::getOrder));
        }

        // Retain categories in sorted order (by Category.order field)
        List<Map.Entry<String, Category>> sortedEntries = new ArrayList<>(categories.entrySet());
        sortedEntries.sort(Comparator.comparingInt(e -> e.getValue().getOrder()));

        categories.clear();
        for (Map.Entry<String, Category> entry : sortedEntries) {
            categories.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Recursively walks the /questions directory and loads content.
     */
    private void loadAllQuestionsRecursively() throws IOException {
        URL questionsUrl = getClass().getResource(QUESTIONS_PATH);
        if (questionsUrl == null) {
            System.err.println("Questions folder not found at: " + QUESTIONS_PATH);
            return;
        }

        try {
            URI uri = questionsUrl.toURI();
            Path rootPath;
            FileSystem fileSystem = null;

            if (uri.getScheme().equals("jar")) {
                fileSystem = getFileSystem(uri);
                rootPath = fileSystem.getPath(QUESTIONS_PATH);
            } else {
                rootPath = Paths.get(uri);
            }

            // Global counter for categories without a number prefix
            // Start high to put them after numbered categories (like 01-basics)
            final int[] autoOrder = { 100 };

            try (Stream<Path> walk = Files.walk(rootPath)) {
                List<Path> directories = walk.filter(Files::isDirectory)
                        .sorted(Comparator.comparing(Path::toString))
                        .collect(Collectors.toList());

                for (Path dir : directories) {
                    processDirectory(dir, rootPath, autoOrder);
                }
            }

        } catch (URISyntaxException e) {
            throw new IOException("Invalid URI for questions folder", e);
        }
    }

    private void processDirectory(Path dir, Path root, int[] autoOrder) {
        // Check if directory has any markdown files (excluding README)
        boolean hasQuestions = false;
        try (Stream<Path> files = Files.list(dir)) {
            hasQuestions = files.anyMatch(p -> {
                String name = p.getFileName().toString();
                return name.endsWith(".md") && !name.equalsIgnoreCase("README.md");
            });
        } catch (IOException e) {
            System.err.println("Failed to list files in " + dir);
            return;
        }

        if (!hasQuestions) {
            return;
        }

        Category category;
        String folderName = dir.getFileName().toString();

        if (dir.equals(root)) {
            // Files directly in /questions root
            category = new Category("general", "General", 9999);
        } else {
            // Create category from folder name
            category = createCategoryFromFolder(folderName, autoOrder[0]);
            autoOrder[0]++;
        }

        // Add category if not exists (deduplicate)
        if (!categories.containsKey(category.getId())) {
            categories.put(category.getId(), category);
        } else {
            category = categories.get(category.getId());
        }

        try {
            // Load questions
            loadQuestionsFromFolder(dir, root, category);
        } catch (IOException e) {
            System.err.println("Error loading questions from " + dir + ": " + e.getMessage());
        }
    }

    /**
     * Creates a category from a folder name like "01-basics" or "240119_Lab1".
     */
    private Category createCategoryFromFolder(String folderName, int defaultOrder) {
        String id;
        String name;
        int order = defaultOrder;

        // Remove leading number prefix (e.g., "01-", "02-") if present
        Pattern prefixPattern = Pattern.compile("^(\\d+)[-_]?(.*)$");
        Matcher matcher = prefixPattern.matcher(folderName);

        if (matcher.matches()) {
            try {
                order = Integer.parseInt(matcher.group(1));
            } catch (NumberFormatException e) {
                // Keep default order
            }
            String remaining = matcher.group(2);
            if (remaining.trim().isEmpty()) {
                id = folderName.toLowerCase().replace("-", "_").replace(" ", "_");
                name = formatCategoryName(folderName);
            } else {
                id = remaining.toLowerCase().replace("-", "_").replace(" ", "_");
                name = formatCategoryName(remaining);
            }
        } else {
            // No prefix
            id = folderName.toLowerCase().replace("-", "_").replace(" ", "_");
            name = formatCategoryName(folderName);
        }

        return new Category(id, name, order);
    }

    /**
     * Formats a category name for display.
     */
    private String formatCategoryName(String name) {
        // Replace hyphens and underscores with spaces
        name = name.replace("-", " ").replace("_", " ");

        // Handle special cases
        name = name.replace("1d", "1D").replace("2d", "2D");

        // Capitalize each word
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : name.toCharArray()) {
            if (Character.isWhitespace(c)) {
                capitalizeNext = true;
                result.append(c);
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    /**
     * Loads all questions from a specific category folder.
     */
    private void loadQuestionsFromFolder(Path folder, Path root, Category category) throws IOException {
        try (Stream<Path> paths = Files.list(folder)) {
            paths.filter(p -> p.toString().endsWith(".md"))
                    .filter(p -> !p.getFileName().toString().equalsIgnoreCase("README.md"))
                    .sorted(Comparator.comparing(p -> extractQuestionNumber(p.getFileName().toString())))
                    .forEach(p -> loadQuestion(p, root, category));
        }
    }

    /**
     * Extracts the question number from filename for sorting.
     */
    private int extractQuestionNumber(String fileName) {
        Pattern pattern = Pattern.compile("Question(\\d+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        Pattern anyNum = Pattern.compile("(\\d+)");
        Matcher anyMatch = anyNum.matcher(fileName);
        if (anyMatch.find()) {
            return Integer.parseInt(anyMatch.group(1));
        }
        return Integer.MAX_VALUE;
    }

    /**
     * Loads a question from a markdown file into a specific category.
     */
    private void loadQuestion(Path path, Path root, Category category) {
        try {
            String content;
            // Use getResourceAsStream to ensure compatibility with JAR packaging
            String relativePath = root.relativize(path).toString().replace("\\", "/");
            String resourcePath = QUESTIONS_PATH + "/" + relativePath;

            try (java.io.InputStream is = getClass().getResourceAsStream(resourcePath)) {
                if (is != null) {
                    content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                } else {
                    // Fallback for development environment or if resource path construction fails
                    content = Files.readString(path, StandardCharsets.UTF_8);
                }
            }

            String fileName = path.getFileName().toString();

            int questionNumber = extractQuestionNumber(fileName);
            String title = markdownParser.extractTitle(content);
            if (title == null || title.isEmpty()) {
                title = fileName.replace(".md", "");
            }

            // Create unique ID combining category and question number
            String id = category.getId() + "_q" + fileName.replace(".md", "").replaceAll("\\s+", "");

            Question question = new Question(id, title, category.getId(), path, questionNumber);
            question.setMarkdownContent(content);

            // Extract test cases
            List<TestCase> testCases = markdownParser.extractTestCases(content);
            question.setTestCases(testCases);

            // Extract hint if present
            String hint = markdownParser.extractHint(content);
            if (hint != null) {
                question.setHint(hint);
            }

            // Extract starter code if present
            String starterCode = markdownParser.extractStarterCode(content);
            if (starterCode != null) {
                question.setStarterCode(starterCode);
            }

            category.addQuestion(question);
            questions.add(question);

        } catch (IOException e) {
            System.err.println("Failed to load question: " + path + " - " + e.getMessage());
        }
    }

    /**
     * Gets the content of a question's markdown file as HTML.
     */
    public String getQuestionHtml(Question question) {
        return getQuestionHtml(question, true);
    }

    /**
     * Gets the content of a question's markdown file as HTML.
     */
    public String getQuestionHtml(Question question, boolean darkMode) {
        // Use the question's folder as the base path for resolving relative images
        Path folderPath = question.getMarkdownPath().getParent();
        String basePath = folderPath.toUri().toString();

        // Ensure strictly no trailing slash if MarkdownParser adds one,
        // OR let it be double slash (browsers don't care).
        // But for cleanliness:
        if (basePath.endsWith("/")) {
            basePath = basePath.substring(0, basePath.length() - 1);
        }

        return markdownParser.toHtml(question.getMarkdownContent(), basePath, darkMode);
    }

    public Collection<Category> getCategories() {
        return categories.values();
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public Question getQuestionById(String id) {
        return questions.stream()
                .filter(q -> q.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public MarkdownParser getMarkdownParser() {
        return markdownParser;
    }

    private FileSystem getFileSystem(URI uri) throws IOException {
        try {
            return FileSystems.getFileSystem(uri);
        } catch (FileSystemNotFoundException e) {
            return FileSystems.newFileSystem(uri, Collections.emptyMap());
        }
    }
}
