package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;

public final class Buttons {

  private Buttons(){

  }
    public static JButton createImageButton(String imagePath, int width, int height, ActionListener action){
        JButton button = new JButton();

        URL url = Buttons.class.getResource(imagePath);

        if (url != null){
            // טוען תמונה מה-resources ומקטין אותה לגודל הרצוי
            ImageIcon icon = new ImageIcon(url);
            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImage));

        }



        // הופך את הכפתור ל"שקוף" (רק התמונה תיראה)
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);

        // פעולה בלחיצה
        if (action != null) {
            button.addActionListener(action);
        }
        return button;
    }









}
