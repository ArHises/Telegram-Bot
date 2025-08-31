package view;

import analysis.AnalysisService;
import analysis.ChartPanel;
import bot.BotInitializer;

import javax.swing.*;
import java.awt.*;

public class SurveyFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private BotInitializer botInitializer;

    private GptInputPanel gptInputPanel;
    private ManualSurvey dynamicSurvey;
    private SelectionPanel selectionPanel;

    private AnalysisService analysisService;
    private ChartPanel chartPanel;

    public SurveyFrame() {

        botInitializer = null;
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        analysisService = new AnalysisService();
        chartPanel = analysisService.getChartPanel();
        selectionPanel = new SelectionPanel(this);
        dynamicSurvey = new ManualSurvey(this , botInitializer);
        gptInputPanel = new GptInputPanel(this , botInitializer);

        cardPanel.add(chartPanel, "charts");
        cardPanel.add(gptInputPanel , "gptInput");
        cardPanel.add(selectionPanel , "select");
        cardPanel.add(dynamicSurvey, "manualInput");

        add(cardPanel);
        cardLayout.show(cardPanel, "select");

        setSize(1200, 800);
//        setExtendedState(JFrame.MAXIMIZED_BOTH);
//        setUndecorated(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public void switchToCharts() {
        chartPanel.setPaused(false);
        analysisService.addSurvey(gptInputPanel.getSurvey() == null ? dynamicSurvey.getSavedSurvey() : gptInputPanel.getSurvey());
        cardLayout.show(cardPanel, "charts");
    }

    public void switchToGptInput() {
        chartPanel.setPaused(true);
        cardLayout.show(cardPanel, "gptInput");
    }

    public void switchToManualInput() {
        chartPanel.setPaused(true);
        cardLayout.show(cardPanel, "manualInput");
    }

    public void switchToSelection() {
        chartPanel.setPaused(true);
        cardLayout.show(cardPanel, "selection");
    }
}
