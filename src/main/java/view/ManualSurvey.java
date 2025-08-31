package view;

import bot.BotInitializer;
import model.Question;
import model.Survey;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static util.Utils.createBotInit;
import static view.Buttons.createImageButton;

public class ManualSurvey extends JPanel {
    private static final int MAX_QUESTIONS = 3;

    private final JPanel questionsContainer = new JPanel();
    private final JButton addQuestionBtn = createImageButton("/addQuestionButton.png", 180, 120, null);
    private final JButton submitButton = createImageButton("/submitButton.png", 180, 120, null);

    private final List<JTextField> questionInputs = new ArrayList<>();
    private final List<AnswerPanel> answerGroups = new ArrayList<>();
    private int questionCount = 0;

    private Survey savedSurvey; // Stores the created Survey object
    private JTextField delayField = new JTextField("0", 5); // Field for delay in minutes

    public ManualSurvey(SurveyFrame surveyFrame, BotInitializer botInitializer) {
        super(new GridBagLayout()); // Use GridBagLayout for centralization

        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.NONE;

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(new JLabel("Delay (minutes): "));
        topPanel.add(delayField);
        add(topPanel, gbc);

        gbc.gridy++;
        questionsContainer.setLayout(new BoxLayout(questionsContainer, BoxLayout.Y_AXIS));
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(questionsContainer);
        scrollPane.setPreferredSize(new Dimension(500, 350));
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(scrollPane);
        add(centerPanel, gbc);

        gbc.gridy++;
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER));
        controls.add(addQuestionBtn);
        controls.add(submitButton);
        add(controls, gbc);

        addQuestionBtn.addActionListener(e -> addQuestionBlock());
        submitButton.addActionListener(e -> {
            savedSurvey = getDataFromFields();
            if(savedSurvey == null) return; // Validation failed
            createBotInit(botInitializer, savedSurvey);
            System.out.println(savedSurvey);
            surveyFrame.switchToCharts();
        });

        addQuestionBlock();
    }

    private void addQuestionBlock() {
        if (questionCount >= MAX_QUESTIONS) return;
        JPanel block = new JPanel();
        block.setLayout(new BoxLayout(block, BoxLayout.Y_AXIS));
        block.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        JTextField questionField = new JTextField(28);
        AnswerPanel answers = new AnswerPanel();
        block.add(new JLabel("Question " + (questionCount + 1) + ":"));
        block.add(Box.createVerticalStrut(6));
        block.add(questionField);
        block.add(Box.createVerticalStrut(6));
        block.add(answers);
        questionInputs.add(questionField);
        answerGroups.add(answers);
        questionsContainer.add(block);
        questionsContainer.add(Box.createVerticalStrut(16)); // Separation between questions
        questionsContainer.revalidate();
        questionsContainer.repaint();
        questionCount++;
        addQuestionBtn.setEnabled(questionCount < MAX_QUESTIONS);
    }

    private Survey getDataFromFields(){
        List<String> questions = getQuestions();
        List<List<String>> allAnswers = getAllAnswers();
        int delay = 0;
        try {
            delay = Integer.parseInt(delayField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid delay value. Please enter a number.");
            return null;
        }
        // Validation: 1 to 3 questions
        int validQuestions = 0;
        for (int i = 0; i < questions.size(); i++) {
            String questionText = questions.get(i).trim();
            List<String> answers = (i < allAnswers.size()) ? allAnswers.get(i) : new ArrayList<>();
            // Remove empty and duplicate answers
            List<String> filteredAnswers = answers.stream()
                    .map(String::trim)
                    .filter(a -> !a.isEmpty())
                    .distinct()
                    .toList();
            if (!questionText.isEmpty() && filteredAnswers.size() >= 2 && filteredAnswers.size() <= 4) {
                validQuestions++;
            }
        }
        if (validQuestions < 1 || validQuestions > 3) {
            JOptionPane.showMessageDialog(this, "You must provide 1 to 3 questions, each with 2 to 4 unique, non-empty answers.");
            return null;
        }
        String topic = "Manual Survey"; // TODO: Replace with actual topic
        Survey survey = new Survey(topic, delay);
        for (int i = 0; i < questions.size(); i++) {
            String questionText = questions.get(i).trim();
            List<String> answers = (i < allAnswers.size()) ? allAnswers.get(i) : new ArrayList<>();
            // Remove empty and duplicate answers
            List<String> filteredAnswers = answers.stream()
                    .map(String::trim)
                    .filter(a -> !a.isEmpty())
                    .distinct()
                    .toList();
            if (!questionText.isEmpty() && filteredAnswers.size() >= 2 && filteredAnswers.size() <= 4) {
                Question q = new Question(questionText, filteredAnswers);
                survey.addQuestion(q);
            }
        }
        return survey;
    }

    public List<String> getQuestions() {
        List<String> questions = new ArrayList<>();
        for (JTextField field : questionInputs) {
            questions.add(field.getText().trim());
        }
        return questions;
    }

    public List<List<String>> getAllAnswers() {
        List<List<String>> allAnswers = new ArrayList<>();
        for (AnswerPanel answerPanel : answerGroups) {
            allAnswers.add(answerPanel.getAnswers());
        }
        return allAnswers;
    }

    public Survey getSavedSurvey() {
        return savedSurvey;
    }
}