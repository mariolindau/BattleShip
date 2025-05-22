package views.panels;

import javax.swing.*;
import java.awt.*;

public class InfoBoard extends JPanel {

    private  JPanel pnlComponent = new JPanel( new GridBagLayout());
    private GridBagConstraints gbc = new GridBagConstraints();

    // kaks kirjastiili
    private Font fontBold = new Font("Verdana", Font.BOLD, 14);
    private Font fontNormal = new Font("Verdana", Font.PLAIN, 14);

    // Võimalikud mängulaua suurused
    private String[] boardSizes = {"10", "11", "12", "13", "14", "15"};

    // Defineerime sildid (Label) muutujad
    private JLabel lblMouseXY;
    private JLabel lblLID;
    private JLabel lblRowCol;
    private JLabel lblTime;
    private JLabel lblShip;
    private JLabel lblGameBoard;
    // Combobox valimaks mängulaua suurus
    private JComboBox<String> cmbSize;
    // Nupudu uus mäng ja edetabel
    private JButton btnNewGame;
    private JButton btnScoreBoard;

    // TODO edetabeliga seotud asjad

    public InfoBoard() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setPreferredSize(new Dimension(400, 100));
        setBackground(new Color(239, 175, 11));

        // Sellele paneelile lähevad kõik komponendid
        pnlComponent.setBackground(new Color(230, 230, 170));

        gbc.anchor = GridBagConstraints.WEST; // = 17 Joondamine vasakult (lahtris)
        gbc.insets = new Insets(2, 2, 2, 2); // Ümber teksti 2px tühjust

        setupLine1();
        setupLine2();
        setupLine3();
        setupLine4();
        setupLine5();
        setupLine6();
        setupComboBox();
        setupButtons();
        add(pnlComponent);
    }

    private void setupLine1() {
        // Esimese rea esimene veerg (rasvane label)
        JLabel label = new JLabel( "Hiir");
        label.setFont(fontBold);
        gbc.gridx = 0; // Veerg
        gbc.gridy = 0; // Rida
        pnlComponent.add(label, gbc);

        // Esimese rea esimene veerg (tavaline label)
        lblMouseXY = new JLabel("x= 0 & y = 0");
        lblMouseXY.setFont(fontNormal);
        gbc.gridx = 1;
        gbc.gridy = 0;
        pnlComponent.add(lblMouseXY, gbc);


    }

    private void setupLine2() {
        JLabel label = new JLabel("Lahtri ID");
        label.setFont(fontBold);
        gbc.gridx = 0; // Veerg
        gbc.gridy = 1; // Rida
        pnlComponent.add(label, gbc);

        lblLID = new JLabel("Teadmata");
        lblLID.setFont(fontNormal);
        gbc.gridx = 1;
        gbc.gridy = 1;
        pnlComponent.add(lblLID, gbc);
    }

    private void setupLine3() {
        JLabel label = new JLabel("Rida: Veerg");
        label.setFont(fontBold);
        gbc.gridx = 0;
        gbc.gridy = 2;
        pnlComponent.add(label, gbc);

        lblRowCol = new JLabel("0 : 0");
        lblRowCol.setFont(fontNormal);
        gbc.gridx = 1;
        gbc.gridy = 2;
        pnlComponent.add(lblRowCol, gbc);
    }

    private void setupLine4() {
        JLabel label = new JLabel("Mängu aeg");
        label.setFont(fontBold);
        gbc.gridx = 0;
        gbc.gridy = 3;
        pnlComponent.add(label, gbc);

        lblTime = new JLabel("00:00");
        lblTime.setFont(fontNormal);
        gbc.gridx = 1;
        gbc.gridy = 3;
        pnlComponent.add(lblTime, gbc);



    }

    private void setupLine5() {
        JLabel label = new JLabel("Laevad");
        label.setFont(fontBold);
        gbc.gridx = 0;
        gbc.gridy = 4;
        pnlComponent.add(label, gbc);

        lblShip = new JLabel("0/0");
        lblShip.setFont(fontNormal);
        gbc.gridx = 1;
        gbc.gridy = 4;
        pnlComponent.add(lblShip, gbc);
    }

    private void setupLine6() {
        JLabel jLabel = new JLabel("Laua suurus");
        jLabel.setFont(fontBold);
        gbc.gridx = 0;
        gbc.gridy = 5;
        pnlComponent.add(jLabel, gbc);

        lblGameBoard = new JLabel("10 x 10");
        lblGameBoard.setFont(fontNormal);
        gbc.gridx = 1;
        gbc.gridy = 5;
        pnlComponent.add(lblGameBoard, gbc);
    }

    private void setupComboBox() {
        JLabel label = new JLabel( "Vali laua suurus");
        label.setFont(fontBold);
        gbc.gridx = 0; // Veerg
        gbc.gridy = 6; // Rida
        pnlComponent.add(label, gbc);

        cmbSize = new JComboBox(boardSizes);
        cmbSize.setFont(fontNormal);
        cmbSize.setPreferredSize(new Dimension(106, 30));
        gbc.gridx = 1;
        gbc.gridy = 6;
        pnlComponent.add(cmbSize, gbc);
    }

    private void setupButtons() {
        JLabel label = new JLabel("Nupud");
        label.setFont(fontBold);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridheight = 2; //Ühendab kaks rida üheks
        gbc.gridwidth = 1; // Üks veerg
        gbc.anchor = GridBagConstraints.WEST; // Paigutus keskele
        gbc.fill = GridBagConstraints.NONE; // Ei venita labelit
        pnlComponent.add(label, gbc);

        // Nupp uus mäng
        btnNewGame = new JButton("Uus mäng");
        btnNewGame.setFont(fontNormal);
        btnNewGame.setPreferredSize(new Dimension(106, 30));
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.gridheight = 1; // Alljärgneval on juba ära määratud ja ei ole vaja seega antud rida
        pnlComponent.add(btnNewGame, gbc);

        // Nupp edetabel
        btnScoreBoard = new JButton("Edetabel");
        btnScoreBoard.setFont(fontNormal);
        btnScoreBoard.setPreferredSize(new Dimension(106, 30));
        gbc.gridx = 1;
        gbc.gridy = 8;
        pnlComponent.add(btnScoreBoard, gbc);
    }
}
