package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DynamicSurvey extends JPanel {
    private static final int MAX_QUESTIONS = 4;

    private final JPanel questionsContainer = new JPanel();
    private final JButton addQuestionBtn = new JButton("Add Question");
    private final JButton runBtn = new JButton("Run");

    // keep data-only accessors; no model types used
    private final ArrayList<JTextField> questionInputs = new ArrayList<>();
    private final ArrayList<AnswerPanel> answerGroups = new ArrayList<>();
    private int questionCount = 0;

    public DynamicSurvey(SurveyFrame surveyFrame) {
        super(new BorderLayout());
        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        questionsContainer.setLayout(new BoxLayout(questionsContainer, BoxLayout.Y_AXIS));
        questionsContainer.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        JScrollPane scroll = new JScrollPane(
                questionsContainer,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );
        scroll.setBorder(new EmptyBorder(12, 12, 12, 12));
        add(scroll, BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        controls.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        controls.add(addQuestionBtn);
        controls.add(runBtn);
        add(controls, BorderLayout.SOUTH);

        addQuestionBtn.addActionListener(e -> addQuestionBlock());
        runBtn.addActionListener( e -> {
            //TODO: create and pass the survey to ChartPanel...
//            surveyFrame.switchToCharts();
        });

        // start with one block
        addQuestionBlock();
    }

    /** Let parent (e.g., SurveyFrame) decide what to do on Run */
    public void addRunListener(ActionListener l) {
        runBtn.addActionListener(l);
    }

    private void addQuestionBlock() {
        if (questionCount >= MAX_QUESTIONS) return;
        int qNumber = ++questionCount;

        JPanel block = new JPanel();
        block.setLayout(new BoxLayout(block, BoxLayout.Y_AXIS));
        block.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        block.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel qLabel = new JLabel("Question " + qNumber + ":");
        JTextField qField = new JTextField();
        qField.setColumns(28);

        JPanel qRow = new JPanel(new BorderLayout(6, 6));
        qRow.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        qRow.add(qLabel, BorderLayout.LINE_START);
        qRow.add(qField, BorderLayout.CENTER);

        // Answers UI (no logic)
        AnswerPanel answers = new AnswerPanel();

        block.add(qRow);
        block.add(Box.createVerticalStrut(8));
        block.add(answers);
        block.add(Box.createVerticalStrut(12));

        questionsContainer.add(block);
        questionsContainer.add(Box.createVerticalStrut(10));

        questionInputs.add(qField);
        answerGroups.add(answers);

        addQuestionBtn.setEnabled(questionCount < MAX_QUESTIONS);

        questionsContainer.revalidate();
        questionsContainer.repaint();
    }

    // ===== Raw data getters (optional for parent to use) =====
    public java.util.ArrayList<String> getQuestions() {
        java.util.ArrayList<String> qs = new java.util.ArrayList<>(questionInputs.size());
        for (JTextField f : questionInputs) qs.add(f.getText().trim());
        return qs;
    }

    public java.util.ArrayList<java.util.ArrayList<String>> getAllAnswers() {
        java.util.ArrayList<java.util.ArrayList<String>> all = new java.util.ArrayList<>(answerGroups.size());
        for (AnswerPanel ap : answerGroups) all.add(ap.getAnswers());
        return all;
    }
}
