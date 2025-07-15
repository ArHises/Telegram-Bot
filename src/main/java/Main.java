import ai.AiSurveyGenerator;
import ai.ChatGptClient;
import model.Survey;
import model.User;

public class Main {
    public static void main(String[] args) {
        System.out.println("Cleaning memory... " + ChatGptClient.postClear());
        System.out.println("balance: " + ChatGptClient.getBalance() + " calls left");

        Survey survey = AiSurveyGenerator.generateSurvey("Capitals", new User(1, "Garik"), 5);
        System.out.println(survey);


        // check all users
//        User.getOrCreateUser(1902,"beni");
//        System.out.println("All users:");
//        for (User user : User.getAllUsers()) {
//            System.out.println(user);
//        }
//
//        // check specific user
//        User check = User.getUserById(1);
//        if (check != null) {
//            System.out.println("User with ID 1 exists: " + check.getUsername());
//        } else {
//            System.out.println("User with ID 1 not found.");
//        }
//    }
    }
}
