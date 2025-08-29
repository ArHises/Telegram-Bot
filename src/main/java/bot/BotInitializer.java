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

        } catch (Exception e) {
            System.err.println("General Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void startDelayThread(BotService service , BotController controller , TelegramBot bot){
        long waitMillis = 0L;
        if (survey.getDelay() != null){
            waitMillis = Math.max(0L , survey.getDelay().getTime() - System.currentTimeMillis());
        }
//        Thread thread = new Thread(() -> { we need to warp it in try catch
//            if (waitMillis > 0L){
//                Thread.sleep(waitMillis);
//            }
//            if (!service.hasActiveSurvey()){
//                return;
//            }
//             for (Long chatId : service.getChatIds()){
//                 controller.startSurveyForUser(chatId , bot);
//             }
//        }).start();
    }

    public Survey getSurvey() {
        return survey;
    }
}
