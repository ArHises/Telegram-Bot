package view;

import ai.AiSurveyGenerator;
import bot.BotInitializer;
import model.Survey;
import model.User;

import javax.swing.*;

public class GptInputPanel extends JPanel {
    private Survey survey;

    public GptInputPanel(SurveyFrame surveyFrame , BotInitializer botInitializer){
        JTextField textField = new JTextField(20);
        JLabel label = new JLabel("please write topic");
        add(label);
        add(textField);

        JButton button = new JButton("submit");

        button.addActionListener(e -> {
            createSurvey(textField.getText());
            createBotInit(botInitializer);
            System.out.println(survey);
            surveyFrame.switchToCharts();
        });

        add(button);
    }

    private void createBotInit(BotInitializer botInitializer){
        botInitializer = new BotInitializer(survey);
        botInitializer.startBot();
    }

    private void createSurvey(String topic){
        survey = AiSurveyGenerator.generateSurvey(topic ,
                new User(5,5,"test") , 5);
    }

    public Survey getSurvey() {
        return survey;
    }
}
