package bot;

import ai.AiSurveyGenerator;
import model.Survey;
import model.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.stickers.CreateNewStickerSet;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import util.Keys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TelegramBot extends TelegramLongPollingBot {

    private final BotController controller;

    public TelegramBot(BotController controller) {
        this.controller = controller;
    }

    public void onUpdateReceived(Update update){
        controller.handleUpdate(update , this);
    }


    public String getBotToken(){
        return Keys.MY_BOT_TOKEN;
    }

    @Override
    public String getBotUsername() {
        return Keys.MY_USERNAME;
    }
}
