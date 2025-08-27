package view;

import ai.AiSurveyGenerator;
import bot.BotInitializer;
import model.Survey;
import model.User;

import javax.swing.*;
import java.awt.*;

import static view.Buttons.createImageButton;

public class GptInputPanel extends BackgroundPanel {
    private Survey survey;

    public GptInputPanel(SurveyFrame surveyFrame , BotInitializer botInitializer){
        super("/TelegramPhoto.jpg");

        setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));

        JLabel label = new JLabel("please write topic");
        label.setForeground(Color.WHITE);                         // לבן
        label.setFont(label.getFont().deriveFont(Font.BOLD, 25f)); // מודגש + גדול

        JTextField textField = new JTextField(25);               // אותו אורך (20)
        Dimension dimension = textField.getPreferredSize();           // מעבים רק את הגובה
        dimension.height = 39;                                        // נסי 36–40 לפי טעם
        textField.setPreferredSize(dimension);

        add(label);
        add(textField);




        JButton submitButton = createImageButton("/submitButton.png",200,200,e->{


            createSurvey(textField.getText());
            createBotInit(botInitializer);
            System.out.println(survey);
            surveyFrame.switchToCharts();

                }

        );
        add(submitButton);

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
