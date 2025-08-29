package bot;

import model.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BotController {

    private final BotService botService;

    private final int MIN_MEMBERS = 2; // we need to change to 3!!! don't forget dalia

    private final Set<Long> waitingToStart = new HashSet<>(); // אני רוצה את זה בשביל שיקבלו סקר אוטומטי אחרי שכולם מוכנים

    public BotController(BotService botService){
        this.botService = botService;
    }

    public void handleUpdate(Update update , TelegramBot bot){
        try {
            if (update.hasMessage() && update.getMessage().hasText()){
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
            handleSurveyState(bot, chatId);
            return;
        }
        if (!botService.getActiveSurvey().isAvailable()) {
            String timeLeft = botService.getActiveSurvey().getTimeUntilAvailable();
            bot.execute(new SendMessage(String.valueOf(chatId), "The survey will start in: " + timeLeft));
            return;
        }
        User user = extractUserFromUpdate(update);
        boolean isNewMember = botService.registerIfNew(user);
        if (isNewMember){
            announceNewMember(bot , chatId , username);
        }
        int size = botService.getChatIds().size();
        if (size < MIN_MEMBERS){
            handleMemberCount(bot, chatId, size);
            return;
        }
        if (!waitingToStart.isEmpty()){
            handleWaitingToStart(bot, chatId);
            return;
        }
        startSurveyForUser(bot, chatId, isNewMember, username);
    }

    private User extractUserFromUpdate(Update update) {
        long chatId = update.getMessage().getChatId();
        String username = update.getMessage().getFrom().getUserName();
        if (username == null) {
            username = "unknown";
        }
        return new User((int) chatId, chatId, username);
    }

    private void handleSurveyState(TelegramBot bot, long chatId) throws TelegramApiException {
        if (!botService.shouldCloseSurvey()) {
            replyClickButtons(bot , chatId);
        } else {
            replyStartInstructions(bot , chatId);
        }
    }

    private void handleMemberCount(TelegramBot bot, long chatId, int size) throws TelegramApiException {
        waitingToStart.add(chatId);
        int remaining = MIN_MEMBERS - size;
        String message = "We need at least " + MIN_MEMBERS + " community members to start the survey. "
                + "Currently in community: " + size + "/" + MIN_MEMBERS + ". "
                + "Waiting for " + remaining + " more… Ask others to send Hi, /start or היי.";
        bot.execute(new SendMessage(String.valueOf(chatId), message));
    }

    private void handleWaitingToStart(TelegramBot bot, long chatId) throws TelegramApiException {
        waitingToStart.add(chatId);
        kickoffWaiting(bot);
    }

    private void startSurveyForUser(TelegramBot bot, long chatId, boolean isNewMember, String username) throws TelegramApiException {
        boolean surveyStarted = botService.startSurveyForChat(chatId);
        System.out.println("is a new Member: " + isNewMember);
        System.out.println("survey started: " + surveyStarted);
        if (surveyStarted) {
            Integer currentIndex = botService.getCurrentIndex(chatId);
            if (currentIndex != null) {
                sendQuestion(bot, chatId, currentIndex);
            }else {
                replyFinished(bot , chatId);
            }
        }else {
            replyFinished(bot , chatId);
        }
    }

    private void announceNewMember(TelegramBot bot, long chatId, String username) throws TelegramApiException {
        String joinAnnouncement = botService.newMemberAnnouncement(username);
        for (Long memberChatId : botService.getChatIds()) {
            if (!memberChatId.equals(chatId)) {
                bot.execute(new SendMessage(String.valueOf(memberChatId), joinAnnouncement));
            }
        }
    }

    private void kickoffWaiting(TelegramBot bot) throws TelegramApiException {
        List<Long> waitingParticipants = new ArrayList<>(waitingToStart);
        waitingToStart.clear();
        for (Long participantChatId : waitingParticipants) {
            boolean started = botService.startSurveyForChat(participantChatId);
            if (started) {
                bot.execute(new SendMessage(String.valueOf(participantChatId),
                        "All required members joined. Starting the survey now!"));
                Integer currentQuestionIndex = botService.getCurrentIndex(participantChatId);
                if (currentQuestionIndex != null) {
                    sendQuestion(bot, participantChatId, currentQuestionIndex);
                } else {
                    replyFinished(bot, participantChatId);
                }
            } else {
                replyFinished(bot, participantChatId);
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
        User voter = new User(1 , chatId , voterUserName); //TODO change the 1

       if (stopIfClosed(bot , chatId)){
           return;
       }

        boolean hasMoreQuestions = botService.submitAnswerAndNext(voter , chatId , chosenAnswer);
        if (hasMoreQuestions){
            Integer currentIndex = botService.getCurrentIndex(chatId);
            if (currentIndex != null){
                sendQuestion(bot , chatId , currentIndex);
            }
        }else {
            replyFinished(bot , chatId);
            System.out.println("Survey completed for chat " + chatId + ":\n" + botService.getActiveSurvey());
        }
    }

    private void sendQuestion(TelegramBot bot, long chatId, Integer questionIndex) throws TelegramApiException {
        String getQuestionText = botService.getQuestion(questionIndex);
        List<String> answerOptions = botService.getOptionsForIndex(questionIndex);

        if (getQuestionText == null || answerOptions == null){
            replyFinished(bot , chatId);
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

    private boolean stopIfClosed(TelegramBot bot, long chatId) throws TelegramApiException {
        if (botService.shouldCloseSurvey()) {
            replyFinished(bot, chatId);
            return true;
        }
        return false;
    }

    private void replyFinished(TelegramBot bot, long chatId) throws TelegramApiException {
        bot.execute(new SendMessage(String.valueOf(chatId), "The survey has finished"));
    }

    private void replyClickButtons(TelegramBot bot, long chatId) throws TelegramApiException {
        bot.execute(new SendMessage(String.valueOf(chatId), "Please click one of the buttons"));
    }

    private void replyStartInstructions(TelegramBot bot, long chatId) throws TelegramApiException {
        bot.execute(new SendMessage(String.valueOf(chatId), "To start the survey please write : Hi , /Start or היי"));
    }
}
