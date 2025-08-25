package view;

import ai.ChatGptClient;
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

        gptInputPanel = new GptInputPanel(this , botInitializer);

        cardPanel.add(chartPanel, "charts");
        cardPanel.add(gptInputPanel , "gptInput");
        cardPanel.add(selectionPanel , "select");


        add(cardPanel);
        cardLayout.show(cardPanel, "select");

        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

    }

    public void switchToCharts() {
        chartPanel.setPaused(false);
        cardLayout.show(cardPanel, "charts");
    }

    public void switchToGptInput() {
        chartPanel.setPaused(false);
        cardLayout.show(cardPanel, "gptInput");
    }

    public void switchToSelection() {
        cardLayout.show(cardPanel, "selection");
    }
}
