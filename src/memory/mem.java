package memory;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class mem {
	public final static JFrame frame = new JFrame("Memory game");
	public final static int rows = 4, cols = 4, delay = 1000;
	public static JLabel labelAttempts = new JLabel();
    public final static JPanel grid = new JPanel(new GridLayout(0, cols, 5, 5));
    public final static int totalButtons = rows * cols;
    public final static ArrayList<JToggleButton> buttons = new ArrayList<>(totalButtons);
    public final static int totalPairs = totalButtons / 2;
    public final static String[] imageUrl = {"img/image1.jpg", "img/image2.jpg", "img/image3.jpg", "img/image4.jpg", "img/image5.jpg", "img/image6.jpg", "img/image7.jpg", "img/image8.jpg"};
	 
    private static class RoundRectIcon implements Icon {
        private final int pairId;
        private final ImageIcon imageIcon;

        public RoundRectIcon(final int pairId,final String imagePath) {
            this.pairId = pairId;
            this.imageIcon = new ImageIcon(imagePath);
        }

        public int getPairId() {
        	return pairId;
        }
        @Override
        public void paintIcon(final Component c, final Graphics g, final int x, final int y) {
            Image scaleImage = imageIcon.getImage().getScaledInstance(getIconWidth(), getIconHeight(), Image.SCALE_DEFAULT);
            ImageIcon scaledImageIcon = new ImageIcon(scaleImage);
            scaledImageIcon.paintIcon(c, g, x, y);
        }

        @Override
        public int getIconWidth() {
            return 50;
        }

        @Override
        public int getIconHeight() {
            return 50;
        }
    }
    
    private static class MemoryGameActionListener implements ActionListener {
        
        private final Timer revertTimer;
        private JToggleButton button1, button2;
        private int pairsCount;
        private int attempsLeft = 3;

        public MemoryGameActionListener(final int delay,final int pairsCount, final int initialAttemps) {
            if (pairsCount <= 0)
                throw new IllegalArgumentException("Non positive pairsCount.");
            this.pairsCount = pairsCount;
            this.attempsLeft = initialAttemps;
            button1 = button2 = null;
            revertTimer = new Timer(delay, this);
            revertTimer.setRepeats(false);
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            revertTimer.stop();

            final Object source = e.getSource();
            if (source instanceof JToggleButton) {
                final JToggleButton button = (JToggleButton) source;
                if (button.isSelected()) {
                    if (button1 == null) //If we are in an initial state:
                        button1 = button; //Store the first clicked button.
                    else if (button2 == null) { //Else we have stored the first button, so store and check the second one...
                        button2 = button;
                        final RoundRectIcon icon1 = (RoundRectIcon) button1.getSelectedIcon(),
                                            icon2 = (RoundRectIcon) button2.getSelectedIcon();
                        if (Objects.equals(icon1.getPairId(), icon2.getPairId())) {
                        	System.out.println("Pair of " + icon1.getPairId() + " found!");
                        	System.out.println("Pair of " + icon2.getPairId() + " found!");
                            button1.setEnabled(false); //Don't let the user able to click it again.
                            button2.setEnabled(false); //Don't let the user able to click it again.
                            button1 = button2 = null; //A cycle is complete.
                            if (--pairsCount == 0) {
                            	JOptionPane.showMessageDialog(null, "Vous avez trouvé toutes les paires !", "Victoire !", JOptionPane.INFORMATION_MESSAGE);
                            	resetGame();
                            }
                        }
                        else {
                        	attempsLeft = attempsLeft - 1;
                        	if(attempsLeft == 0) {
                        		JOptionPane.showMessageDialog(null, "Plus d'essaie !", "Perdu !", JOptionPane.INFORMATION_MESSAGE);
                        		resetGame();
                        	}
                        	updateAttemptsLabel(attempsLeft);
                            revertTimer.start(); //This timer will unselect the selected button pair after a delay.
                        }
                    }
                    else { //Else both buttons are stored, so this is the third one, so just ignore the previous buttons (if they did not match):
                        if (button1.isEnabled())
                            button1.setSelected(false);
                        if (button2.isEnabled())
                            button2.setSelected(false);
                        button1 = button; //Suppose now that the third clicked button is the first one.
                        button2 = null;
                    }
                }
                else { //Else the user unselected a button, so reset:
                    if (button1 != null) {
                        button1.setSelected(false);
                        button1 = null;
                    }
                    if (button2 != null) {
                        button2.setSelected(false);
                        button2 = null;
                    }
                }
            }
            else if (source == revertTimer) {
                button1.setSelected(false);
                button2.setSelected(false);
                button1 = button2 = null;
            }
            else
                System.err.println("Unknown source " + source);
        }
    }
   
    public static void updateAttemptsLabel(int attempts) {
    	labelAttempts.setText("Essais restants : " + attempts);
    }
 
    private static JToggleButton createStandardToggleButton(final MemoryGameActionListener listener, final int pairId, final String image) {
        final JToggleButton toggle = new JToggleButton(new RoundRectIcon(pairId, "img/default.png")); //Default (ie enabled + unselected) icon is just a white round rect.
        toggle.setSelectedIcon(new RoundRectIcon(pairId, image));
        toggle.setDisabledIcon(toggle.getSelectedIcon());
        toggle.setDisabledSelectedIcon(toggle.getSelectedIcon());
        toggle.addActionListener(listener);
        return toggle;
    }
    
    private static void initializeButtons(ArrayList<JToggleButton> buttons, MemoryGameActionListener listener, String[] imageUrl, JPanel grid, int totalPairs) {
        final int[] index = {0};
        final int[] array = new int[totalPairs];
        for (int i = 0; i < array.length; i++) {
            buttons.add(createStandardToggleButton(listener, i, imageUrl[i]));
            buttons.add(createStandardToggleButton(listener, i, imageUrl[i]));
        }
        Collections.shuffle(buttons);
        buttons.forEach(button -> grid.add(button));
    }
    public static void resetGame () {
    	final int attempsNumber = 3;
    	final MemoryGameActionListener listener = new MemoryGameActionListener(delay, totalPairs,attempsNumber);
    	updateAttemptsLabel(attempsNumber);
    	 grid.removeAll();
         buttons.clear();
         listener.pairsCount = totalPairs;
         initializeButtons(buttons, listener, imageUrl, grid, totalPairs);
         grid.revalidate();
         grid.repaint();
    }

    private static void createAndShowGUI() {
       
    	resetGame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(grid);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        //TOP PANEL 
        JPanel panelTop = new JPanel();
        JButton buttonRestart = new JButton("Restart");
        buttonRestart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               resetGame();
            }
        });
        panelTop.setLayout(new GridLayout(0, 1, 0, 0));
        panelTop.add(buttonRestart);
        
        JPanel panel = new JPanel();
        panelTop.add(panel);
        JLabel labelTitle = new JLabel("Temps :");
        panel.add(labelTitle);
        JLabel labelTime = new JLabel("20,5");
        panel.add(labelTime);
        frame.getContentPane().add(panelTop, BorderLayout.NORTH);

        
        //BOTTOM PANEL
        JPanel panelBottom = new JPanel();
        panelBottom.add(labelAttempts);
        frame.getContentPane().add(panelBottom, BorderLayout.SOUTH);
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(mem::createAndShowGUI);
    }
}