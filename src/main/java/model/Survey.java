package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Survey {

    private String topic;
    private Date createdAt;
    private Date delay;
    private long timeToClose;
    private List<Question> questions;

    private boolean isFinished = false;

    public Survey(String topic, int delay) {
        this.createdAt = new Date(System.currentTimeMillis());
        this.delay = new Date(System.currentTimeMillis()
                + (long) delay * 1000 * 60);
        this.topic = topic;
        this.questions = new ArrayList<>();
        this.timeToClose = this.delay.getTime() + 5 * 60 * 1000; // 5 minutes after available
    }

    public boolean isAvailable() {
        return new Date().after(delay);
    }

    public String getTimeUntilAvailable() {
        return calcTimeLeft(this.delay.getTime());
    }

    public String getTimeUntilClose(){
        if (this.timeToClose == 0) return "00:00:00";
        return calcTimeLeft(this.timeToClose);
    }

    private String calcTimeLeft(long time){
        if (time == 0) return "00:00:00";
        long millis = time - System.currentTimeMillis();
        if (millis <= 0) return "00:00:00";
        long totalSeconds = millis / 1000;
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public long getTimeToClose() {
        return timeToClose;
    }

    public void addQuestion(Question question){
        this.questions.add(question);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        this.isFinished = finished;
    }

    @Override
    public String toString() {
        return "Survey{" +
                ", \ntopic='" + topic + '\'' +
                ", \ncreatedAt=" + createdAt +
                ", \ndelay=" + delay +
                ", \nquestions=" + questions +
                "\n}";
    }
}