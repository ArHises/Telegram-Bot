package view;

import analysis.AnalysisService;
import analysis.ChartPanel;

import javax.swing.*;
import java.awt.*;

public class SurveyFrame extends JFrame {

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
        JFrame window = new JFrame();
        window.setSize(1000 , 1000);
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setLocationRelativeTo(null);
        window.add(new SelectionPanel());

        window.setVisible(true);
    }

    public void switchToCharts() {
        chartPanel.setPaused(false); // מחליף מסך לCHARTS
        cardLayout.show(cardPanel, "charts");
    }
}
