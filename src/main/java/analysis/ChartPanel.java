package analysis;

import model.Question;
import model.Survey;
import model.User;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class ChartPanel extends JPanel {

    private Survey survey;
    private ChartRenderer chartRenderer; // מעבד תרשימים

    public ChartPanel(Survey survey) {
        setBackground(new Color(173,216,230));

        this.survey = survey;
        this.chartRenderer = new ChartRenderer(this);
        chartRenderer.start();
    }

    @Deprecated
    public void drawColumns(Graphics g, int x, int y){
        int gap = 0;
        Random rand = new Random();
        for (int i = 0; i < this.survey.getQuestions().size(); i++){
            Question currentQuestion = this.survey.getQuestions().get(i);
            Map<String, List<User>> answers = currentQuestion.getAnswers();
            for (Map.Entry<String, List<User>> entry : answers.entrySet()){
                Color randomColor = new Color(rand.nextInt(80,200),
                        rand.nextInt(80,200),
                        rand.nextInt(80,200));

                g.setColor(randomColor);
                g.fillRect(x, y + gap, entry.getValue().size() * 50, 20);
                gap += (20 + 20);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw delay at the top center
        if (survey != null) {
            String delayText = survey.isAvailable()
                    ? "Survey available until: " + survey.getTimeUntilClose()
                    : "Survey available in: " + survey.getTimeUntilAvailable();
            g.setFont(g.getFont().deriveFont(Font.BOLD, 22f));
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(delayText);
            int x = (getWidth() - textWidth) / 2;
            int y = 40;
            g.setColor(Color.BLACK);
            g.drawString(delayText, x, y);
        }
        drawPies(g);
    }

    public void drawPies(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        int numberOfQuestions = this.survey.getQuestions().size();
        for (int i = 0; i < numberOfQuestions; i++){
            DefaultPieDataset dataset = new DefaultPieDataset();
            Question currentQuestion = this.survey.getQuestions().get(i);
            Map<String, List<User>> answers = currentQuestion.getAnswers();
            for (Map.Entry<String, List<User>> entry : answers.entrySet()){
                dataset.setValue(entry.getKey(), entry.getValue().size());
            }
            int pieWidth = getWidth() / numberOfQuestions;
            int pieHeight = getHeight() / numberOfQuestions;
            JFreeChart pie = ChartFactory.createPieChart(currentQuestion.getQuestion(),
                    dataset, false, false, false);
            PiePlot plot = (PiePlot) pie.getPlot();
            plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {2}"));
            pie.draw(g2, new Rectangle(pieWidth * i, 60, pieWidth, pieHeight));
        }
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public void setPaused(boolean paused) {
        chartRenderer.setPaused(paused);
    }
}
