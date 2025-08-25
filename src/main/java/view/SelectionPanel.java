package view;

import javax.swing.*;
import java.awt.*;

public class SelectionPanel extends JPanel {
    private Image backgroundImage;

    public SelectionPanel(SurveyFrame surveyFrame){
        backgroundImage = new ImageIcon(getClass().getResource("/telegram.jpeg"))
                .getImage();
        JTextArea textArea = new JTextArea("Dear user! \n" +
                "To \u200B\u200Bbe able to answer the survey you have 2 options \n" +
                "The first - choose a manual survey \n" +
                "The second - choose a survey via Chat Gpt");
        textArea.setEditable(false);
        textArea.setOpaque(false);
        textArea.setFont(new Font("Survey gpt" , Font.BOLD , 30));
        textArea.setForeground(Color.white);
        textArea.setHighlighter(null);
        textArea.setFocusable(false);
        add(textArea);

        JButton button = new JButton("go to chatGpt");
        button.addActionListener(e -> {
            surveyFrame.switchToGptInput();
        });
        add(button);
    }

    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        graphics.drawImage( backgroundImage , 0 , 0 , getWidth(), getHeight() , this);
    }

}
