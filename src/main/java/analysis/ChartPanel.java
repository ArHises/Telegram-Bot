package analysis;

import model.Question;
import model.Survey;
import model.User;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ChartPanel extends JPanel {

    private Survey survey;
    private ChartRenderer chartRenderer; // מעבד תרשימים

    private final int startX = 50;
    private final int startY = 50;

    public ChartPanel(Survey survey) {
        this.survey = survey;
        this.chartRenderer = new ChartRenderer(this);
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

    public void drawPies(Graphics g){
        Graphics2D g2 = (Graphics2D) g;

        List<JFreeChart> pies = new ArrayList<>();
        int numberOfQuestions = this.survey.getQuestions().size();
        Random rand = new Random();
        for (int i = 0; i < numberOfQuestions; i++){
            DefaultPieDataset dataset = new DefaultPieDataset();
            Question currentQuestion = this.survey.getQuestions().get(i);
            Map<String, List<User>> answers = currentQuestion.getAnswers();
            int numberOfAnswers = answers.size();

            for (Map.Entry<String, List<User>> entry : answers.entrySet()){
                float splitPie = ((float) (entry.getValue().size() * 100) / numberOfAnswers);
                dataset.setValue(entry.getKey(), splitPie);
            }
            int pieWidth = getWidth() / numberOfQuestions;
            int pieHeight = getHeight() / numberOfQuestions;
            JFreeChart pie = ChartFactory.createPieChart(currentQuestion.getQuestion(),
                    dataset, false, false, false);
            pie.draw(g2, new Rectangle(pieWidth * i, 0, pieWidth, pieHeight));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        drawColumns(g,this.startX,this.startY);
        drawPies(g);
    }

    public void setPaused(boolean paused) {
        chartRenderer.setPaused(paused);
    }
}
