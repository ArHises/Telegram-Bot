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
import util.MyKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TelegramBot extends TelegramLongPollingBot {

    private final BotController controller;

    public TelegramBot(BotController controller) {
        this.controller = controller;
    }

    public void onUpdateReceived(Update update){

    }




//    public void onUpdateReceived(Update update) {
//        if (update.hasCallbackQuery()){
//            this.currentQuestionIndex++;
//            if (this.currentQuestionIndex < this.survey.getQuestions().size()){ // all the other calls
//                handleButtonClick(update.getCallbackQuery());
//            }else {
//                SendMessage sendMessage = new SendMessage();
//                sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
//                sendMessage.setText("Survey completed");
//                System.out.println(this.survey);
//                try {
//                    execute(sendMessage);
//                } catch (TelegramApiException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }else {
//            currentQuestionIndex = 0;
//            long chatId = update.getMessage().getChatId();
//            sendQuestion(chatId); // our first call
//        }
//    }
//
//    private void handleButtonClick(CallbackQuery callbackQuery){
//        String callbackData = callbackQuery.getData();
//        long chatId = callbackQuery.getMessage().getChatId();
//
//        if (callbackData == null){
//            AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
//            answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
//            answerCallbackQuery.setText("Error!");
//            try {
//                execute(CreateNewStickerSet.builder().build());
//            } catch (TelegramApiException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        String chosenAnswer = callbackData;
//
//        try {
//            execute(AnswerCallbackQuery.builder()
//                    .callbackQueryId(callbackQuery.getId())
//                    .text("You chose: " + chosenAnswer)
//                    .build());
//        } catch (TelegramApiException e) {
//            throw new RuntimeException(e);
//        }
//
//        if (currentQuestionIndex < survey.getQuestions().size()) {
//            sendQuestion(chatId);
//        } else {
//            try {
//                execute(new SendMessage(String.valueOf(chatId), "Survey completed"));
//            } catch (TelegramApiException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//    }
//
//    public void sendQuestion(long chatId){
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setText(survey.getQuestions().get(this.currentQuestionIndex).getQuestion());
//        // what the capital of italy?
//        sendMessage.setChatId(chatId);
//
//        List<InlineKeyboardButton> row = new ArrayList<>();
//        for (Map.Entry<String, List<User>> entry : survey.getQuestions().get(this.currentQuestionIndex).getAnswers().entrySet()) {
//            InlineKeyboardButton button = new InlineKeyboardButton();
//            button.setText(entry.getKey()); // rome , berlin , tokyo ...
//            row.add(button);
//            button.setCallbackData(entry.getKey());
//        }
//
//        List<List<InlineKeyboardButton>> matrix = new ArrayList<>();
//        matrix.add(row);
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        inlineKeyboardMarkup.setKeyboard(matrix);
//        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
//        try {
//            execute(sendMessage);
//        } catch (TelegramApiException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public String getUsername(Update update){
//        String username = update.getMessage().getFrom().getUserName();
//        return username != null ? username : "unknown";
//    }

    public String getBotToken(){
        return MyKeys.MY_BOT_TOKEN;
    }

    @Override
    public String getBotUsername() {
        return MyKeys.MY_USERNAME;
    }
}
