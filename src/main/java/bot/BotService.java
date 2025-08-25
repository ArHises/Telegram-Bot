package bot;

import model.Survey;
import model.User;
import model.UsersCsv;

import java.util.*;

public class BotService {

    private final UsersCsv usersCsv = new UsersCsv();
    private final Set<Long> chatIds = new HashSet<>();
    private final Map<Long , Integer> progressByChat = new HashMap<>();
    private Survey activeSurvey;



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

    public String newMemberAnnouncement(String userName){ //this method will be used in if statement with registerIfNew = existed method
        int size = chatIds.size();
        return "New member : " + userName + "  Community size : " + size;
    }

    public void setActiveSurvey(Survey survey) {
        this.activeSurvey = survey;
        progressByChat.clear();
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
        if (!hasActiveSurvey()){
            return false;
        }
        Integer currentQuestionIndex = progressByChat.get(chatId);
        if (currentQuestionIndex == null){
            return startSurveyForChat(chatId);
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
            return false;
        }
    }


}
