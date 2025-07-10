package analysis;

import model.Survey;

import java.util.ArrayList;
import java.util.List;

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
    }

    public void addSurvey(Survey survey){
        this.surveys.add(survey);
        this.chartPanel = new ChartPanel(survey);
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