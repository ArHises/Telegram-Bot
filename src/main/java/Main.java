import ai.AiSurveyGenerator;
import ai.ChatGptClient;
import bot.TelegramBot;
import model.Survey;
import model.User;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import view.GptInputPanel;
import view.SelectionPanel;
import view.SurveyFrame;
import view.ManualSurveyConnector;

import javax.swing.*;

import java.awt.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;


//User creator = new User(1, 123456789L, "testuser");

public class Main {
    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(() -> {
            // ONE frame only
            SurveyFrame frame = new SurveyFrame();

            // Build a User from the creator name typed in the dialog
            java.util.function.Function<String, model.User> userFactory =
                    name -> new model.User(1, 123456789L, name); // use the 'name'!

            // Attach connector to THIS frame
            ManualSurveyConnector.attach(
                    frame,
                    userFactory,
                    survey -> {
                        System.out.println("\n=== SURVEY BUILT ===\n" + survey + "\n");
                        javax.swing.JOptionPane.showMessageDialog(
                                frame, survey.toString(), "Survey Built",
                                javax.swing.JOptionPane.INFORMATION_MESSAGE
                        );
                        // e.g. botService.setActiveSurvey(survey);
                    }
            );

            frame.setVisible(true);
        });
    }
}
//        try {
//            // Step 1: Create a User (id, chatId, username)
//            User creator = new User(1, 123456789L, "testuser");
//            // Step 2: Create a Survey (User, topic, delay in minutes)
//            Survey survey = AiSurveyGenerator.generateSurvey("CAPITALS" , creator, 5);
//            System.out.println("API calls left: " + ChatGptClient.getBalance());
//            // Step 3: Create BotService with the Survey
//            BotService botService = new BotService(survey);
//            // Step 4: Create BotController with BotService
//            BotController botController = new BotController(botService);
//            // Step 5: Create TelegramBot with BotController
//            TelegramBot telegramBot = new TelegramBot(botController);
//
//            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
//            telegramBotsApi.registerBot(telegramBot);
//            System.out.println("Bot is running and connected!");
//        } catch (TelegramApiException e) {
//            System.err.println("Telegram API Exception: " + e.getMessage());
//            e.printStackTrace();
//        } catch (Exception e) {
//            System.err.println("General Exception: " + e.getMessage());
//            e.printStackTrace();
//        }
