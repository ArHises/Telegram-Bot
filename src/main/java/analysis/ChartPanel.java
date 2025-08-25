package analysis;

import model.Survey;

import javax.swing.*;
import java.awt.*;

public class ChartPanel extends JPanel {

    private Survey survey;
    private ChartRenderer chartRenderer; // מעבד תרשימים

    public ChartPanel(Survey survey) {
        this.survey = survey;
        this.chartRenderer = new ChartRenderer(this);
    }

    public void updateCharts() {
    }

    @Override
    public void repaint(Rectangle r) {
        super.repaint(r);
    }

    public void setPaused(boolean paused) {
        chartRenderer.setPaused(paused);
    }
}
