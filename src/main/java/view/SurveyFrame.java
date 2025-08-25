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

    private final DynamicSurvey dynamicPanel = new DynamicSurvey();
    private final SelectionPanel selectionPanel = new SelectionPanel();

    // top bar shown only on "builder"
    private final JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private final JButton backBtn = new JButton("Back to Menu");

    public SurveyFrame() {
        super("Survey");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setSize(1000, 1000);
        setLocationRelativeTo(null);
        setVisible(true);

        cardLayout = new CardLayout();
        cardPanel  = new JPanel(cardLayout);

        analysisService = new AnalysisService();
        chartPanel      = analysisService.getChartPanel();

        // cards
        cardPanel.add(selectionPanel, "menu");
        cardPanel.add(dynamicPanel,   "builder");
        cardPanel.add(chartPanel,     "charts");

        // wire menu button -> open builder & show top bar
        selectionPanel.addOpenManualListener(e -> {
            cardLayout.show(cardPanel, "builder");
            topBar.setVisible(true);
            revalidate(); repaint();
        });

        // top bar (hidden on menu)
        backBtn.addActionListener(e -> {
            cardLayout.show(cardPanel, "menu");
            topBar.setVisible(false);
            revalidate(); repaint();
        });
        topBar.add(backBtn);
        topBar.setVisible(false); // start hidden

        // layout
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topBar,   BorderLayout.NORTH);
        getContentPane().add(cardPanel, BorderLayout.CENTER);

        // show menu first
        cardLayout.show(cardPanel, "menu");
    }

    public void switchToCharts() {
        chartPanel.setPaused(false);
        cardLayout.show(cardPanel, "charts");
    }
}