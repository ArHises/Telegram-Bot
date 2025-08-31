package bot;

import model.Survey;
import model.User;
import model.UsersCsv;

import java.text.SimpleDateFormat;
import java.util.*;

public class BotService {

    private final UsersCsv usersCsv = new UsersCsv();
    private final Set<Long> chatIds = new HashSet<>();
    private final Set<Long> finishedChatIds = new HashSet<>();
    private final Map<Long , Integer> progressByChat = new HashMap<>();
    private Survey activeSurvey;
    private long openUntilMillis = 0L;

    public static final long SURVEY_TIMEOUT_MILLIS = 5 * 60 * 1000L;

    public BotService(Survey initialSurvey){
        this.activeSurvey = initialSurvey;
    }

    public boolean registerIfNew(User user){
        long chatId = user.getChatId();
        if (chatIds.contains(chatId)){
            return false;
        }

        boolean isExistInCsv = false;
        List<User> allUsers = usersCsv.getAllUsers();
        for (int i = 0; i <allUsers.size() ; i++) {
            User currentUser = allUsers.get(i);
            if (currentUser.getChatId() == chatId){
                isExistInCsv = true;
                break;
            }
        }
        if (!isExistInCsv){
            usersCsv.addUser(user);
        }
        chatIds.add(chatId);
        return !isExistInCsv;
    }

    public Set<Long> getChatIds(){// אולי למחוק לא נחוץ
        return Collections.unmodifiableSet(chatIds);
    }

    public String newMemberAnnouncement(String userName){
        int size = chatIds.size();
        return "New member : " + userName + "  Community size : " + size;
    }

    public boolean shouldCloseSurvey() {
        boolean timeUp = (openUntilMillis > 0) && (System.currentTimeMillis() >= openUntilMillis);
        boolean allFinished = (openUntilMillis > 0) && !chatIds.isEmpty()
                && finishedChatIds.containsAll(chatIds) ;
        return timeUp || allFinished;
    }

    public void endSurvey(){
        this.openUntilMillis = 0L;
        this.progressByChat.clear();
        this.finishedChatIds.clear();
        this.activeSurvey = null;
        System.out.println("Survey ended.");
    }

    public Survey getActiveSurvey() {
        return activeSurvey;
    }

    public boolean hasActiveSurvey() {
        return activeSurvey != null
                && activeSurvey.getQuestions() != null
                && !activeSurvey.getQuestions().isEmpty();
    }

    public boolean startSurveyForChat(long chatId){
        if (!hasActiveSurvey()){
            return false;
        }
        if (openUntilMillis == 0L){
//            openUntilMillis = System.currentTimeMillis() + SURVEY_TIMEOUT_MILLIS;
            openUntilMillis = activeSurvey.getTimeToClose();

//            activeSurvey.setTimeToClose(openUntilMillis);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String formattedTime = sdf.format(new Date(openUntilMillis));
            System.out.println("survey will be finished at: " + formattedTime);
        }
        if (shouldCloseSurvey()){
            return false;
        }
        if (finishedChatIds.contains(chatId)){
            return false;
        }
        progressByChat.put(chatId , 0);
        return true;
    }

    public Integer getCurrentIndex(Long chatId){
        return progressByChat.get(chatId);
    }

    private boolean isValidQuestionIndex(int index){
        return hasActiveSurvey() && index >= 0 &&
                index < activeSurvey.getQuestions().size();
    }

    public String getQuestion(int questionIndex){
        if (!isValidQuestionIndex(questionIndex)){
            return "invalid question index";
        }
        return activeSurvey.getQuestions().get(questionIndex).getQuestion();
    }

    public List<String> getOptionsForIndex(int questionIndex){
        if (!isValidQuestionIndex(questionIndex)){
            return null;
        }
        return new ArrayList<>(activeSurvey.getQuestions().get(questionIndex).getAnswers().keySet());
    }

    public boolean submitAnswerAndNext(User voter , Long chatId , String chosenAnswer){
        if (shouldCloseSurvey()){
            return false;
        }
        if (finishedChatIds.contains(chatId)){
            return false;
        }
        Integer currentQuestionIndex = progressByChat.get(chatId);
        if (currentQuestionIndex == null){
            return false;
        }
        if (!isValidQuestionIndex(currentQuestionIndex)){
            return false;
        }
        activeSurvey.getQuestions().get(currentQuestionIndex).addVote(chosenAnswer , voter);

        int nextQuestionIndex = currentQuestionIndex + 1;
        if (nextQuestionIndex < activeSurvey.getQuestions().size()){
            progressByChat.put(chatId , nextQuestionIndex);
            return true;
        }else {
            progressByChat.remove(chatId);
            finishedChatIds.add(chatId);
            if (shouldCloseSurvey()){
                endSurvey();
            }
            return false;
        }
    }
}
