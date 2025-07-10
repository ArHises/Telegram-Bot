package view;

import analysis.AnalysisService;
import analysis.ChartPanel;

import javax.swing.*;
import java.awt.*;

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

        cardPanel.add(chartPanel, "charts");

        add(cardPanel);
        cardLayout.show(cardPanel, "Main Menu");
    }

    public void switchToCharts() {
        chartPanel.setPaused(false);
        cardLayout.show(cardPanel, "Instruction");
    }
}
