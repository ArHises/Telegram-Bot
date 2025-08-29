package view;

import javax.swing.*;
import java.awt.*;

public class AnswerPanel extends JPanel {
    private static final int MAX_ANSWERS = 4;
    private static final int START_ANSWERS = 2;

    private final JPanel answersPanel = new JPanel();
    private JButton addAnswerBtn; // image button

    private final java.util.ArrayList<JTextField> answerFields = new java.util.ArrayList<>();

    public AnswerPanel() {
        setLayout(new BorderLayout(8, 8));
        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        answersPanel.setLayout(new BoxLayout(answersPanel, BoxLayout.Y_AXIS));
        answersPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        // start with 2 answers
        for (int i = 0; i < START_ANSWERS; i++) addAnswerField();

        add(answersPanel, BorderLayout.CENTER);

        // Image button for "Add Answer"
        addAnswerBtn = Buttons.createImageButton(
                "/add_answer_button.png", 90, 36,
                e -> {
                    if (answerFields.size() < MAX_ANSWERS) addAnswerField();
                    updateAddEnabled();
                }
        );
        addAnswerBtn.setToolTipText("Add Answer");

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnRow.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        btnRow.setOpaque(false);
        btnRow.add(addAnswerBtn);
        add(btnRow, BorderLayout.SOUTH);

        updateAddEnabled();
    }

    private void addAnswerField() {
        int idx = answerFields.size() + 1;

        JPanel row = new JPanel(new BorderLayout(6, 6));
        row.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        row.setOpaque(false);

        JLabel label = new JLabel("Answer " + idx + ":");
        JTextField field = new JTextField();
        field.setColumns(24);

        row.add(label, BorderLayout.LINE_START);
        row.add(field, BorderLayout.CENTER);

        answerFields.add(field);
        answersPanel.add(row);
        answersPanel.add(Box.createVerticalStrut(6));

        revalidate();
        repaint();
    }

    private void updateAddEnabled() {
        if (addAnswerBtn != null) {
            addAnswerBtn.setEnabled(answerFields.size() < MAX_ANSWERS);
        }
    }

    public java.util.ArrayList<String> getAnswers() {
        java.util.ArrayList<String> out = new java.util.ArrayList<>(answerFields.size());
        for (JTextField f : answerFields) out.add(f.getText().trim());
        return out;
    }
}

