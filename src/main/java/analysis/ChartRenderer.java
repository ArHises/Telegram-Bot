package analysis;

public class ChartRenderer extends Thread {

    private final ChartPanel chartPanel;

    private boolean running = true;
    private volatile boolean paused = true;

    public ChartRenderer(ChartPanel chartPanel) {
        this.chartPanel = chartPanel;
//        start();
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    @Override
    public void run() {
        new Thread(() -> {
            while (running) {
                if (!paused) {
                    this.chartPanel.repaint();
                }
                try {
                    Thread.sleep(16); // Roughly 60 FPS (1000 ms / 60 ≈ 16.67)
                } catch (InterruptedException ignored) {}
            }
        }).start();
    }
}
