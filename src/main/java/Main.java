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
        System.out.println("Cleaning memory... " + ChatGptClient.postClear());
        System.out.println("balance: " + ChatGptClient.getBalance() + " calls left");

        Survey survey = AiSurveyGenerator.generateSurvey("Capitals", new User(1, "Garik"), 5);
        System.out.println(survey);

        TelegramBotsApi telegramBotsApi = null;
        try {
            telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new TelegramBot());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        // check all users
//        User.getOrCreateUser(1902,"beni");
//        System.out.println("All users:");
//        for (User user : User.getAllUsers()) {
//            System.out.println(user);
//        }
//
//        // check specific user
//        User check = User.getUserById(1);
//        if (check != null) {
//            System.out.println("User with ID 1 exists: " + check.getUsername());
//        } else {
//            System.out.println("User with ID 1 not found.");
//        }
//    }
    }
}
