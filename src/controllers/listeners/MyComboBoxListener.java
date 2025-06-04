package controllers.listeners;

import models.Model;
import views.View;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class MyComboBoxListener implements ItemListener {
    private Model model;
    private View view;

    public MyComboBoxListener(Model model, View view) {
    this.model = model;
    this.view = view;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
       // System.out.println(e.getItem());; // Test
        if(e.getStateChange() == ItemEvent.SELECTED) {
            // System.out.println(e.getItem());; //Test
            String number = e.getItem().toString(); // Võta väärtus tekstina (String)
            int size = Integer.parseInt(number); // Tee eelnev number mis on stringina => täisarvuks
            // view.getLblGameBoard().setText(number + " x " + number);
            view.getLblGameBoard().setText(String.format("%d x %d", size, size)); // Kui soovid number asemel size kasutada (read 25 ja 26)
            model.setBoardSize(size); // Määrab mudelis mängulaua suuruse
            view.pack(); // Et suurus muutuks
            view.repaint(); // Joonista uuesti

        }
    }
}
