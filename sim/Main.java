import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    private static final int WINDOW_WIDTH = PongGame.WINDOW_WIDTH;
    private static final int WINDOW_HEIGHT = PongGame.WINDOW_HEIGHT;
    
    public static void main(String[] args) {
        // Launch the menu
        SwingUtilities.invokeLater(() -> {
            JFrame menuFrame = new JFrame("Game Menu");
            menuFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            
            // Create and show menu
            JPanel center = new JPanel();
            center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

            JLabel title = new JLabel("Main Menu", SwingConstants.CENTER);
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            title.setFont(new Font("SansSerif", Font.BOLD, 20));

            JButton startBtn = new JButton("Start Game");
            JButton exitBtn = new JButton("Exit");

            Dimension btnSize = new Dimension(140, 36);
            startBtn.setMaximumSize(btnSize);
            exitBtn.setMaximumSize(btnSize);

            startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

            startBtn.addActionListener(e -> launchGame(menuFrame));
            exitBtn.addActionListener(e -> System.exit(0));

            center.add(Box.createVerticalGlue());
            center.add(title);
            center.add(Box.createRigidArea(new Dimension(0, 12)));
            center.add(startBtn);
            center.add(Box.createRigidArea(new Dimension(0, 8)));
            center.add(exitBtn);
            center.add(Box.createVerticalGlue());

            menuFrame.add(center, BorderLayout.CENTER);
            menuFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            menuFrame.setLocationRelativeTo(null);
            menuFrame.setVisible(true);
        });
    }

    private static void launchGame(JFrame menuFrame) {
        JFrame gameFrame = new JFrame("Pong");
        gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        PongGame game = new PongGame();
        game.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        gameFrame.add(game);
        gameFrame.pack();
        gameFrame.setResizable(false);
        gameFrame.setVisible(true);
        gameFrame.setLocationRelativeTo(null);

        Timer timer = new Timer(33, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.gameLogic();
                game.repaint();
            }
        });

        timer.start();
        menuFrame.dispose();
    }
}