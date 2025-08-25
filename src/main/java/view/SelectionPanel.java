package view;

import javax.swing.*;
import java.awt.*;

public class SelectionPanel extends JPanel {
    private Image backgroundImage;
    private final JButton openManualBtn = new JButton("Open Manual Survey");

    public SelectionPanel() {
        backgroundImage = new ImageIcon(getClass().getResource("/telegram.jpeg")).getImage();

        setLayout(new BorderLayout());

        // TOP: your text (transparent)
        JTextArea textArea = new JTextArea(
                "Dear user! \n" +
                        "To \u200B\u200Bbe able to answer the survey you have 2 options \n" +
                        "The first - choose a manual survey \n" +
                        "The second - choose a survey via Chat Gpt"
        );
        textArea.setEditable(false);
        textArea.setOpaque(false);
        textArea.setFont(new Font("Survey gpt", Font.BOLD, 30));
        textArea.setForeground(Color.white);
        textArea.setHighlighter(null);
        textArea.setFocusable(false);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        top.setOpaque(false);
        top.add(textArea);
        add(top, BorderLayout.NORTH);

        // CENTER: button centered
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.add(openManualBtn, new GridBagConstraints());
        add(center, BorderLayout.CENTER);
    }

    /** Let parent wire the action (e.g., switch to builder card). */
    public void addOpenManualListener(java.awt.event.ActionListener l) {
        openManualBtn.addActionListener(l);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
