package util;

import bot.BotInitializer;
import model.Survey;

public class Utils {
    public static void createBotInit(BotInitializer botInitializer, Survey survey) {
        botInitializer = new BotInitializer(survey);
        botInitializer.startBot();
    }
}
