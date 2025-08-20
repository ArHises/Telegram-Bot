import ai.AiSurveyGenerator;
import ai.ChatGptClient;
import model.Survey;
import model.User;
import model.UsersCsv;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Cleaning memory... " + ChatGptClient.postClear());
        System.out.println("balance: " + ChatGptClient.getBalance() + " calls left");

        Survey survey = AiSurveyGenerator.generateSurvey("Capitals", new User(1,123456789L, "Garik"), 5);
        System.out.println(survey);


        // this is a check for user and usercsv
//        try {
//            Files.deleteIfExists(Paths.get("data/users.csv"));
//        } catch (IOException e) {
//            throw new RuntimeException("Could not reset CSV file", e);
//        }
//        UsersCsv repo = new UsersCsv(); // re-init after delete
        // 1. add users // works
//        repo.addUser(new User(1, 111111111L, "Alice"));
//        repo.addUser(new User(2, 222222222L, "Bob"));
//        repo.addUser(new User(3, 333333333L, "Charlie"));
//        System.out.println("added 3 users");

        // 2. Read users // works
//        List<User> users = repo.getAllUsers();
//        System.out.println("all the users:");
//        users.forEach(System.out::println);


//        // 3. Update Bob’s username // works
//        repo.updateUser(222222222L, "Bobby");
//        System.out.println(" updated bobs username");
//        users = repo.getAllUsers();
//        System.out.println("users after update:");
//        users.forEach(System.out::println);
//
//        // 4. Delete Charlie // works
//        repo.deleteUser(333333333L);
//        System.out.println("deleted charlie");
//        users = repo.getAllUsers();
//        System.out.println("users after delete:");
//        users.forEach(System.out::println);
//
    }
}
