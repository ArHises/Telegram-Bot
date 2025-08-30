package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DynamicSurvey extends JPanel {
    private static final int MAX_QUESTIONS = 4;

    private final JPanel questionsContainer = new JPanel();
    private JButton addQuestionBtn;   // image button
    private JButton runBtn;           // image button

    // data-only
    private final ArrayList<JTextField> questionInputs = new ArrayList<>();
    private final ArrayList<AnswerPanel> answerGroups = new ArrayList<>();
    private int questionCount = 0;

    public DynamicSurvey(SurveyFrame surveyFrame) {
        super(new BorderLayout());
        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        // questions area
        questionsContainer.setLayout(new BoxLayout(questionsContainer, BoxLayout.Y_AXIS));
        questionsContainer.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        JScrollPane scroll = new JScrollPane(
                questionsContainer,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );
        scroll.setBorder(new EmptyBorder(12, 12, 12, 12));
        add(scroll, BorderLayout.CENTER);

        addQuestionBtn = Buttons.createImageButton(
                "/addQuestionButton.png", 120, 130,
                e -> addQuestionBlock()
        );
        addQuestionBtn.setToolTipText("Add Question");

        runBtn = Buttons.createImageButton(
                "/runButton.png", 120, 130,
                null
        );
        runBtn.setToolTipText("Run");

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        controls.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        controls.add(addQuestionBtn);
        controls.add(runBtn);
        add(controls, BorderLayout.SOUTH);

        // start with one block
        addQuestionBlock();
        updateButtonsState();
    }

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

        // Answers UI
        AnswerPanel answers = new AnswerPanel();

        block.add(qRow);
        block.add(Box.createVerticalStrut(8));
        block.add(answers);
        block.add(Box.createVerticalStrut(12));

        questionsContainer.add(block);
        questionsContainer.add(Box.createVerticalStrut(10));

        questionInputs.add(qField);
        answerGroups.add(answers);

        updateButtonsState();
        questionsContainer.revalidate();
        questionsContainer.repaint();
    }

    private void updateButtonsState() {
        if (addQuestionBtn != null) {
            addQuestionBtn.setEnabled(questionCount < MAX_QUESTIONS);
        }
    }

    public ArrayList<String> getQuestions() {
        ArrayList<String> qs = new ArrayList<>(questionInputs.size());
        for (JTextField f : questionInputs) qs.add(f.getText().trim());
        return qs;
    }

    public ArrayList<ArrayList<String>> getAllAnswers() {
        ArrayList<ArrayList<String>> all = new ArrayList<>(answerGroups.size());
        for (AnswerPanel ap : answerGroups) all.add(ap.getAnswers());
        return all;
    }
}

