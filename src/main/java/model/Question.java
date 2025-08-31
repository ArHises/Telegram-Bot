package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Question {

    private String question;
    private Map<String, List<User>> answers;

    public Question(String question, List<String> answers) {
        this.question = question;
        this.answers = answers.stream()
                .collect(Collectors.toMap(
                        answer -> answer,
                        answer -> new ArrayList<User>()
                ));
    }

    /**
     * @param answer the question topic
     * @param user user to add to the topic
     * Add a new vote to question
     */
    public void addVote(String answer,User user){
       for (List<User> voters : answers.values()){
           if (voters.contains(user)){
               return;
           }
       }
       List<User> users = this.answers.get(answer);
       if (users != null){
           users.add(user);
       }
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