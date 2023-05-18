package gui;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame
{

    public GameWindow(ControlOfRobot controller)
    {
        super("Игровое поле", true, true, true, true);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(controller.getVisualizer(), BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}
