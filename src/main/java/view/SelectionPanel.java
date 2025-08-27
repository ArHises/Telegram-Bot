package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static view.Buttons.createImageButton;

public class SelectionPanel extends BackgroundPanel {


    public SelectionPanel(SurveyFrame surveyFrame) {
        super("/TelegramPhoto.jpg");
        setLayout(new BorderLayout());

        // ----- טקסט למעלה -----
        JTextArea textArea = new JTextArea(
                "Dear user,\n" +
                        "To be able to answer the survey you have 2 options:\n" +
                        "1. choose a manual survey.\n" +
                        "2. choose a survey via ChatGpt."
        );
        textArea.setEditable(false);
        textArea.setOpaque(false);
        textArea.setFont(new Font("Survey gpt", Font.BOLD, 30));
        textArea.setForeground(Color.WHITE);
        textArea.setHighlighter(null);
        textArea.setFocusable(false);
        textArea.setMargin(new Insets(30, 160, 10, 10));
        add(textArea, BorderLayout.NORTH);

        // ----- כפתורים -----
        JButton createWithAiButton = createImageButton(
                "/ChatGptSurvey.png", 400, 400,
                e -> {
                    surveyFrame.switchToGptInput(); }
        );

        JButton createManualButton = createImageButton(
                "/ManualSurvey.png", 400, 400,
                e -> {
                    surveyFrame.switchToManualInput(); }
        );

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 50));
        buttonsPanel.setOpaque(false);
        buttonsPanel.add(createWithAiButton);
        buttonsPanel.add(createManualButton);

        // ----- שליטה מדויקת בגובה הכפתורים -----
        int buttonsOffsetPixels = 180; // <<<<<< תשני את המספר כדי להזיז למעלה/למטה
        JPanel positioningPanel = new JPanel();
        positioningPanel.setOpaque(false);
        positioningPanel.setLayout(new BoxLayout(positioningPanel, BoxLayout.Y_AXIS));

        positioningPanel.add(Box.createVerticalStrut(buttonsOffsetPixels)); // רווח מלמעלה
        positioningPanel.add(buttonsPanel);                                  // הכפתורים עצמם
        positioningPanel.add(Box.createVerticalGlue());                      // דוחף את השאר למטה

        add(positioningPanel, BorderLayout.CENTER);
    }





}
