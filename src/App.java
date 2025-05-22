import models.Model;
import views.View;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Model model = new Model();
                View view = new View(model);


                view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                view.pack();
                view.setLocationRelativeTo(null); // ekraani keskele
                view.setVisible(true); // Tee JFrame nähtavaks
            }
        });
    }
}
