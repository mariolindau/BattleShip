import controllers.Controller;
import models.Model;
import views.View;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Model model = new Model();
                View view = new View(model);
                Controller controller = new Controller(model, view);

                // Hiire liikumise aktiveerimiseks
                view.registerGameBoardMouse(controller);

                view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                // Akna miinimum suuruse määramine
                view.setMinimumSize(new java.awt.Dimension(850, 550));
                view.pack();
                view.setLocationRelativeTo(null); // ekraani keskele
                view.setVisible(true); // Tee JFrame nähtavaks
            }
        });
    }
}
