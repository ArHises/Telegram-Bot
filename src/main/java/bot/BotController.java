package bot;

import model.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class BotController {

    private final BotService botService;


    public BotController(BotService botService){
        this.botService = botService;
    }

    public void handleUpdate(Update update , TelegramBot bot){
        try {
            if (update.hasMessage() && update.getMessage().hasText()){
                System.out.println(botService.getActiveSurvey().toString());
                handleStartOrIgnore(update , bot);
            } else if (update.hasCallbackQuery()) {
                handleAnswer(update.getCallbackQuery() , bot);
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException("error" , e);
        }
    }

    private void handleStartOrIgnore(Update update, TelegramBot bot) throws TelegramApiException {
        long chatId = update.getMessage().getChatId();
        String username = update.getMessage().getFrom().getUserName();
        if (username == null) {
            username = "unknown";
        }
        String text = update.getMessage().getText().trim();
        if (!isJoinCommand(text)) {
            bot.execute(new SendMessage(String.valueOf(chatId), "To start the survey write : Hi , /Start or היי"));
            return;
        }

        User user = new User((int) chatId, chatId, username);
        boolean isNewMember = botService.registerIfNew(user);
        boolean surveyStarted = botService.startSurveyForChat(chatId);
        System.out.println(botService.getActiveSurvey().toString()); // testing
        System.out.println("is a new Member: " + isNewMember);
        System.out.println("survey started: " + surveyStarted);
        if (surveyStarted) {
            Integer currentIndex = botService.getCurrentIndex(chatId);
            if (currentIndex != null) {
                sendQuestion(bot, chatId, currentIndex);
            }else {
                System.out.println("survey broken");
                bot.execute(new SendMessage(String.valueOf(chatId) , "The survey is not available"));
            }

            if (isNewMember) {
                String joinAnnouncement = botService.newMemberAnnouncement(username);
                for (Long memberChatId : botService.getChatIds()) {
                    if (!memberChatId.equals(chatId)) {
                        bot.execute(new SendMessage(String.valueOf(memberChatId), joinAnnouncement));
                    }
                }
            }
        }
    }

    private boolean isJoinCommand(String text) {
        return text.equalsIgnoreCase("/start")
                || text.equalsIgnoreCase("hi")
                || text.equals("היי");
    }

    private void handleAnswer(CallbackQuery callbackQuery , TelegramBot bot) throws TelegramApiException {
        long chatId = callbackQuery.getMessage().getChatId();
        String chosenAnswer = callbackQuery.getData();
        String voterUserName = callbackQuery.getFrom().getUserName();
        if (voterUserName == null){
            voterUserName = "Unknown";
        }
        User voter = new User(1 , chatId , voterUserName); //TODO
        boolean hasMoreQuestions = botService.submitAnswerAndNext(voter , chatId , chosenAnswer);
        if (hasMoreQuestions){
            Integer currentIndex = botService.getCurrentIndex(chatId);
            if (currentIndex != null){
                sendQuestion(bot , chatId , currentIndex);
            }
        }else {
            bot.execute(new SendMessage(String.valueOf(chatId) , "The survey has finished"));
            System.out.println("Survey completed for chat " + chatId + ":\n" + botService.getActiveSurvey());
        }
    }

    private void sendQuestion(TelegramBot bot, long chatId, Integer questionIndex) throws TelegramApiException {
        String getQuestionText = botService.getQuestion(questionIndex);
        List<String> answerOptions = botService.getOptionsForIndex(questionIndex);

        if (getQuestionText == null || answerOptions == null){
            bot.execute(new SendMessage(String.valueOf(chatId) , "invalid question or options"));
            return;
        }

        SendMessage message = new SendMessage(String.valueOf(chatId) , getQuestionText);
        List<List<InlineKeyboardButton>> keyboardMatrix = new ArrayList<>();

            for (String option : answerOptions){
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(option);
                button.setCallbackData(option);
                keyboardMatrix.add(List.of(button));
            }

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboardMatrix);
        message.setReplyMarkup(keyboardMarkup);

        bot.execute(message);
    }
}
