package bot;

import model.Survey;
import model.User;
import model.UsersCsv;

import java.util.*;

public class BotService {

    private final UsersCsv usersCsv = new UsersCsv();
    private final Set<Long> chatIds = new HashSet<>();
    private final Map<Long , Integer> progressByChat = new HashMap<>();
    private Survey activeSurvey; // סקר פעיל נוכחי יחיד


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

    public Set<Long> getChatIds(){
        return Collections.unmodifiableSet(chatIds);
    }

    public String newMemberAnnouncement(String userName){
        int size = chatIds.size();
        return "New member : " + userName + "  Community size : " + size;
    }

    public void setActiveSurvey(Survey survey) {
        this.activeSurvey = survey;
        progressByChat.clear();// מאפס מפה של כל המשתמשים
    }

    public Survey getActiveSurvey() {
        return activeSurvey;
    }

    public boolean hasActiveSurvey() {
        return activeSurvey != null
                && activeSurvey.getQuestions() != null
                && !activeSurvey.getQuestions().isEmpty();
    }

    public Integer startSurveyForChat(long chatId){
        if (!hasActiveSurvey()){
            return null;
        }
        progressByChat.put(chatId , 0);
        return 0;
    }

    public Integer getCurrentIndex(Long chatId){
        return progressByChat.get(chatId);
    }

    public String getQuestion(int questionIndex){
        if (!hasActiveSurvey()){
            return "no active survey";
        }
        if (questionIndex < 0 || questionIndex >= activeSurvey.getQuestions().size()){
            return "invalid question index";
        }
        return activeSurvey.getQuestions().get(questionIndex).getQuestion();
    }

    public List<String> getOptionsForIndex(int questionIndex){
        List<String> options = new ArrayList<>();
        if (!hasActiveSurvey()){
            return null;
        }
        if (questionIndex < 0 || questionIndex >= activeSurvey.getQuestions().size()){
            return null;
        }

        Set<String> keys = activeSurvey.getQuestions().get(questionIndex).getAnswers().keySet();
        for (String k : keys){
            options.add(k);
        }
        return options;
    }

    public Integer submitAnswerAndNext(User voter , Long chatId , String chosenAnswer){
        if (!hasActiveSurvey()){
            return null;
        }
        Integer currentQuestionIndex = progressByChat.get(chatId);
        if (currentQuestionIndex == null){
            return startSurveyForChat(chatId);
        }

        int totalQuestions = activeSurvey.getQuestions().size();
        if (currentQuestionIndex < 0 || currentQuestionIndex >= totalQuestions){
            return null;
        }

        activeSurvey.getQuestions().get(currentQuestionIndex).addVote(chosenAnswer , voter);


        int nextQuestionIndex = currentQuestionIndex + 1;
        if (nextQuestionIndex < totalQuestions){
            progressByChat.put(chatId , nextQuestionIndex);
            return nextQuestionIndex;
        }else {
            progressByChat.remove(chatId);
            return null;
        }
    }

    //    TODO: Responsibilities:
//      - Enforce single active survey.
//      - Collect answers and forward them to AnalysisService.
//      - Schedule delayed sends (using a scheduler).
}
