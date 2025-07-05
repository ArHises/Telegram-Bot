import ai.AiSurveyGenerator;
import ai.ChatGptClient;
import model.Survey;
import model.User;

public class Main {
    public static void main(String[] args) {
        System.out.println("Cleaning... " + ChatGptClient.postClear());
        System.out.println("balance: " + ChatGptClient.getBalance());
//        System.out.println(ChatGptClient.getSurvey("what are the best dragons"));

        Survey survey = AiSurveyGenerator.generateSurvey("Dragons",new User(),5);
        System.out.println(survey);
    }
}
