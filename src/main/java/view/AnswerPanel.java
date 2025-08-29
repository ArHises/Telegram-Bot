package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * A panel for entering possible answers to a survey question.
 * Allows 2 to 4 answers, each in its own text field.
 */
public class AnswerPanel extends JPanel {
    private static final int MAX_ANSWERS = 4;
    private static final int START_ANSWERS = 2;

    private final JPanel answersPanel = new JPanel();
    private final JButton addAnswerBtn = new JButton("Add Answer");
    private final ArrayList<JTextField> answerFields = new ArrayList<>();

    /**
     * Constructs an AnswerPanel with 2 answer fields by default.
     */
    public AnswerPanel() {
        setLayout(new BorderLayout(8, 8));
        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        answersPanel.setLayout(new BoxLayout(answersPanel, BoxLayout.Y_AXIS));
        answersPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        // Start with 2 answer fields
        for (int i = 0; i < START_ANSWERS; i++) {
            addAnswerField();
        }

        add(answersPanel, BorderLayout.CENTER);

        addAnswerBtn.addActionListener(e -> {
            if (answerFields.size() < MAX_ANSWERS) {
                addAnswerField();
            }
            addAnswerBtn.setEnabled(answerFields.size() < MAX_ANSWERS);
        });

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnRow.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        btnRow.add(addAnswerBtn);
        add(btnRow, BorderLayout.SOUTH);
    }

    /**
     * Adds a new answer field to the panel.
     */
    private void addAnswerField() {
        int idx = answerFields.size() + 1;

        JPanel row = new JPanel(new BorderLayout(6, 6));
        row.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        JLabel label = new JLabel("Answer " + idx + ":");
        JTextField field = new JTextField();
        field.setColumns(24);

        row.add(label, BorderLayout.LINE_START);
        row.add(field, BorderLayout.CENTER);

        answerFields.add(field);
        answersPanel.add(row);
        answersPanel.add(Box.createVerticalStrut(6)); // Space between answer fields

        revalidate();
        repaint();
    }

    /**
     * Returns the list of answers entered by the user (trimmed).
     * @return ArrayList of answer strings
     */
    public ArrayList<String> getAnswers() {
        ArrayList<String> answers = new ArrayList<>(answerFields.size());
        for (JTextField field : answerFields) {
            answers.add(field.getText().trim());
        }
        return answers;
    }
}
