package view;

import ai.AiSurveyGenerator;
import bot.BotInitializer;
import model.Survey;
import model.User;

import javax.swing.*;
import java.awt.*;

import static util.Utils.createBotInit;
import static view.Buttons.createImageButton;

public class GptInputPanel extends BackgroundPanel {
    private Survey survey;

    public GptInputPanel(SurveyFrame surveyFrame , BotInitializer botInitializer){
        super("/TelegramPhoto.jpg");

        setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));

        JLabel label = new JLabel("please write topic");
        label.setForeground(Color.WHITE);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 25f));

        JTextField textField = new JTextField(25);
        Dimension dimension = textField.getPreferredSize();
        dimension.height = 39;
        textField.setPreferredSize(dimension);

        JLabel delayLabel = new JLabel("Delay (minutes):");
        delayLabel.setForeground(Color.WHITE);
        delayLabel.setFont(delayLabel.getFont().deriveFont(Font.PLAIN, 18f));
        JTextField delayField = new JTextField("0", 5);
        Dimension delayDim = delayField.getPreferredSize();
        delayDim.height = 36;
        delayField.setPreferredSize(delayDim);

        JPanel inputPanel = new JPanel();
        inputPanel.setOpaque(false);
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 16, 0));
        inputPanel.add(label);
        inputPanel.add(textField);
        inputPanel.add(delayLabel);
        inputPanel.add(delayField);
        add(inputPanel);

        JButton submitButton = createImageButton("/submitButton.png",200,200,e->{
            int delayMinutes = 0;
            try {
                delayMinutes = Integer.parseInt(delayField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid delay value. Please enter a number.");
                return;
            }
            createSurvey(textField.getText(), delayMinutes);
            createBotInit(botInitializer, survey);
            System.out.println(survey);
            surveyFrame.switchToCharts();
        });
        add(submitButton);
    }

    private void createSurvey(String topic, int delayMinutes){
        survey = AiSurveyGenerator.generateSurvey(topic ,
                new User(5,5,"test") , delayMinutes);
    }

    public Survey getSurvey() {
        return survey;
    }
}
