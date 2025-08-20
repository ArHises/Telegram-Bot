import ai.AiSurveyGenerator;
import ai.ChatGptClient;
import bot.TelegramBot;
import model.Survey;
import model.User;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {

        TelegramBotsApi telegramBotsApi = null;
        try {
            telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new TelegramBot());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

//        System.out.println("Cleaning memory... " + ChatGptClient.postClear());
//        System.out.println("balance: " + ChatGptClient.getBalance() + " calls left");
//
//        Survey survey = AiSurveyGenerator.generateSurvey("dogs",new User(1,"Batya"),5);
//        System.out.println(survey);
    }
}
