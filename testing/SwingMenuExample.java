package testing;

import javax.swing.*;
import java.awt.*;

public class SwingMenuExample {
    
    private static final int WINDOW_WIDTH = PongGame.WINDOW_WIDTH;
    private static final int WINDOW_HEIGHT = PongGame.WINDOW_HEIGHT;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SwingMenuExample::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Game Menu Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setLocationRelativeTo(null);

        // Simple content area
        JLabel label = new JLabel("Welcome to the game!", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.PLAIN, 18));
        frame.add(label, BorderLayout.CENTER);

        // Center panel with buttons (Start, Settings, About, Exit)
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Main Menu", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));

        JButton startBtn = new JButton("Start");
        JButton settingsBtn = new JButton("Settings");
        JButton aboutBtn = new JButton("About");
        JButton exitBtn = new JButton("Exit");

        Dimension btnSize = new Dimension(140, 36);
        startBtn.setMaximumSize(btnSize);
        settingsBtn.setMaximumSize(btnSize);
        aboutBtn.setMaximumSize(btnSize);
        exitBtn.setMaximumSize(btnSize);

        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        aboutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Actions
        startBtn.addActionListener(e -> label.setText("Game started..."));
        settingsBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "No settings yet."));
        aboutBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Simple Swing menu example."));
        exitBtn.addActionListener(e -> System.exit(0));

        center.add(Box.createVerticalGlue());
        center.add(title);
        center.add(Box.createRigidArea(new Dimension(0, 12)));
        center.add(startBtn);
        center.add(Box.createRigidArea(new Dimension(0, 8)));
        center.add(settingsBtn);
        center.add(Box.createRigidArea(new Dimension(0, 8)));
        center.add(aboutBtn);
        center.add(Box.createRigidArea(new Dimension(0, 8)));
        center.add(exitBtn);
        center.add(Box.createVerticalGlue());

        frame.add(center, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}
