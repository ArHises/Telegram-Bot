package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Question {
    // לכל שאלה יש 2-4 תשובות אפשריות

    private String question; // the question topic
    private Map<String, List<User>> answers;// answers and a list of users that voted the specific answer (2 - 4 answers).

    public Question(String question, List<String> answers) {
        this.question = question;
        this.answers = answers.stream()
                .collect(Collectors.toMap(
                        answer -> answer,
                        answer -> new ArrayList<User>()
                ));
    }

    /**
     * @param question the question topic
     * @param user user to add to the topic
     * Add a new vote to question
     */
    public void addVote(String question,User user){
        List<User> users = this.answers.get(question);
        users.add(user);
        this.answers.put(question,users);
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Map<String, List<User>> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, List<User>> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "\nQuestion{" +
                "\nquestion='" + question + '\'' +
                ", \nanswers=" + answers +
                "\n}";
    }
}