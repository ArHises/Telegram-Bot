package view;

import ai.AiSurveyGenerator;
import analysis.AnalysisService;
import analysis.ChartPanel;
import model.Survey;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.Set;

public class SurveyFrame extends JFrame {
//    TODO: Responsibilities:
//      - Create windows and panels for survey.
//      - Input fields for questions, answer options, topic keyword, and delay (minutes).

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private AnalysisService analysisService;
    private ChartPanel chartPanel;

    public SurveyFrame() {

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        analysisService = new AnalysisService();
        chartPanel = analysisService.getChartPanel();

        cardPanel.add(chartPanel, "Charts");

        add(cardPanel);
        cardLayout.show(cardPanel, "Charts");
    }

    public void switchToCharts() {
        chartPanel.setPaused(false);
        cardLayout.show(cardPanel, "Instruction");
    }
}
