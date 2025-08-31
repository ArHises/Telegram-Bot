import ai.ChatGptClient;
import view.SurveyFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Cleaning memory... " + ChatGptClient.postClear());
        System.out.println("balance: " + ChatGptClient.getBalance() + " calls left");

        SwingUtilities.invokeLater(() -> {
            SurveyFrame surveyFrame = new SurveyFrame();
            surveyFrame.setVisible(true);
        });
    }
}