public class Main {
    public static void main(String[] args) {
        System.out.println("Cleaning memory... " + ChatGptClient.postClear());
        System.out.println("balance: " + ChatGptClient.getBalance() + " calls left");

        Survey survey = AiSurveyGenerator.generateSurvey("Capitals", new User(1, "Garik"), 5);
        System.out.println(survey);

    }
}
