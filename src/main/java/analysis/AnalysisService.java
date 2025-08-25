package analysis;

import ai.AiSurveyGenerator;
import model.Question;
import model.Survey;
import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class AnalysisService {
//    TODO: Responsibilities:
//      - Tally responses per question.
//      - Determine if all have answered early or timeout reached.
//      - Compute percentage distributions, sort options by popularity.

    private List<Survey> surveys;
    private ChartPanel chartPanel;
    private boolean isActive;

    public AnalysisService() {
        this.surveys = new ArrayList<>();
        this.chartPanel = new ChartPanel(null);
        this.isActive = false;

//        Survey survey = AiSurveyGenerator.generateSurvey("Capitals",new User(1,"Garik"),5);
//        System.out.println(survey);
//        voteSimulation(survey);
//        addSurvey(survey);

    }

    @Deprecated
    public void voteSimulation(Survey survey){
        new Thread(() -> {
            Random random = new Random();
            for (int i = 0; i < survey.getQuestions().size(); i++){
                Set<String> answers = survey.getQuestions().get(i).getAnswers().keySet();
                for (String ans : answers){
                    for (int j = 0; j < random.nextInt(1,6); j++) {
//                        survey.getQuestions().get(i).addVote(ans, new User(random.nextInt(),"user" + i + j));
                        try {
                            Thread.sleep(random.nextInt(1000,2000));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        chartPanel.repaint();
                        System.out.println("------ " + (i + j) + " ------");
                        System.out.println(survey);
                    }
                }
            }
        }).start();
    }

    public void addSurvey(Survey survey){
        this.surveys.add(survey);
        this.chartPanel.setSurvey(survey);
        this.isActive = true;
    }

    public Survey getCurrentSurvey() {
        return this.surveys.get(this.surveys.size() - 1);
    }

    public List<Survey> getSurveys() {
        return surveys;
    }

    public boolean isActive() {
        return isActive;
    }

    public ChartPanel getChartPanel() {
        return this.chartPanel;
    }
}