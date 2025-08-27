package bot;

import ai.AiSurveyGenerator;
import ai.ChatGptClient;
import model.Survey;
import model.User;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class BotInitializer {

    private Survey survey;

    public BotInitializer(Survey survey){
        this.survey = survey;
    }

    public void startBot(){
        try {
//            // Step 1: Create a User (id, chatId, username)
//            User creator = new User(1, 123456789L, "testuser");

            // Step 3: Create BotService with the Survey
            BotService botService = new BotService(survey);
            // Step 4: Create BotController with BotService
            BotController botController = new BotController(botService);
            // Step 5: Create TelegramBot with BotController
            TelegramBot telegramBot = new TelegramBot(botController);

            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramBot);
            System.out.println("Bot is running and connected!");

            boolean ok = botService.setActiveSurveyIfAllowed(survey);
            if (!ok){
                System.out.println("Survey not activated : we need 3 or more members and no existing survey");
                return;
            }

            boolean launched = botService.launchSurveyNow();
            System.out.println("survey launched " + launched);


        } catch (TelegramApiException e) {
            System.err.println("Telegram API Exception: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Survey getSurvey() {
        return survey;
    }
}
