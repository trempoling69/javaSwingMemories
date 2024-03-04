package memory;
import java.awt.EventQueue;

import javax.swing.*;
import java.awt.*;

public class layout {

	private JFrame frmFindThePigeon;
    private JPanel panelTop, panelCenter, panelBottom;
    private JLabel labelTitle, labelTime, labelAttempts;
    private JButton[][] buttons;
    private JPanel panel;
    private ImageIcon defaultIcon, clickedIcon;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					layout window = new layout();
					window.frmFindThePigeon.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public layout() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmFindThePigeon = new JFrame("Memory Game");
		frmFindThePigeon.setTitle("Find the pigeon");
        frmFindThePigeon.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmFindThePigeon.setSize(600, 600);
        frmFindThePigeon.getContentPane().setLayout(new BorderLayout());

        // Top Panel
        panelTop = new JPanel();
        JButton buttonOptions = new JButton("Options");
        panelTop.setLayout(new GridLayout(0, 1, 0, 0));
        panelTop.add(buttonOptions);
        
        panel = new JPanel();
        panelTop.add(panel);
        labelTitle = new JLabel("Temps :");
        panel.add(labelTitle);
        labelTime = new JLabel("20,5");
        panel.add(labelTime);
        frmFindThePigeon.getContentPane().add(panelTop, BorderLayout.NORTH);

        defaultIcon = new ImageIcon("img/test.jpg");
        Image scaleImage = defaultIcon.getImage().getScaledInstance(50, 50,Image.SCALE_DEFAULT);
        ImageIcon imageIcon = new ImageIcon(scaleImage);
        // Center Panel
        panelCenter = new JPanel();
        panelCenter.setLayout(new GridLayout(4, 4));
        buttons = new JButton[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setIcon(imageIcon);
                panelCenter.add(buttons[i][j]);
            }
        }
        frmFindThePigeon.getContentPane().add(panelCenter, BorderLayout.CENTER);

        // Bottom Panel
        panelBottom = new JPanel();
        labelAttempts = new JLabel("Essais restants : 3");
        panelBottom.add(labelAttempts);
        frmFindThePigeon.getContentPane().add(panelBottom, BorderLayout.SOUTH);

        frmFindThePigeon.setVisible(true);
	}

}