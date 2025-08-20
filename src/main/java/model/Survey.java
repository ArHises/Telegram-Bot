package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Survey {
// -כל סקר יהיה 1-3 שאלות, כאשר לכל שאלה יש 2-4 תשובות אפשריות.

    private User creator;// the creator of the survey
    private String topic;// the topic of the survey
    private Date createdAt;// the time when the survey was created
    private Date delay;// delayed start in minutes
    private List<Question> questions;// 1 - 3 questions to answer

    public Survey(User creator, String topic, int delay) {
        this.creator = creator;
        this.createdAt = new Date(System.currentTimeMillis());
        this.delay = new Date(System.currentTimeMillis()
                + (long) delay * 1000 * 60);
        this.topic = topic;
        this.questions = new ArrayList<>();
    }

    public void addQuestion(Question question){
        this.questions.add(question);
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getDelay() {
        return delay;
    }

    public void setDelay(Date delay) {
        this.delay = delay;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "Survey{" +
                "\ncreator=" + creator +
                ", \ntopic='" + topic + '\'' +
                ", \ncreatedAt=" + createdAt +
                ", \ndelay=" + delay +
                ", \nquestions=" + questions +
                "\n}";
    }
}