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
            BotService botService = new BotService(survey);
            BotController botController = new BotController(botService);
            TelegramBot telegramBot = new TelegramBot(botController);

            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramBot);
            System.out.println("Bot is running and connected!");
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
