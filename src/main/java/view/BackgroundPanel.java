package view;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class BackgroundPanel extends JPanel {

    private Image backgroundImage;

public BackgroundPanel(String resourcePath){
    setOpaque(false);
    setBackgroundImage(resourcePath);
}

    public void setBackgroundImage(String resourcePath) {
        URL url = getClass().getResource(resourcePath);
        if (url != null){
            backgroundImage = new ImageIcon(url).getImage();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null){
            g.drawImage(backgroundImage,0,0,getWidth(),getHeight(),this);

        }
    }
}
