import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main {
    private static final int WINDOW_WIDTH = PongGame.WINDOW_WIDTH;
    private static final int WINDOW_HEIGHT = PongGame.WINDOW_HEIGHT;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            installLookAndFeel();

            JFrame menuFrame = new JFrame("Pong");
            menuFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            menuFrame.getContentPane().setBackground(new Color(18, 18, 18));
            menuFrame.setLayout(new BorderLayout());

            JLabel title = new JLabel("Pong", SwingConstants.CENTER);
            title.setFont(new Font("Segoe UI", Font.BOLD, 32));
            title.setForeground(Color.WHITE);
            title.setBorder(BorderFactory.createEmptyBorder(24, 0, 12, 0));

            JPanel card = new JPanel();
            card.setOpaque(true);
            card.setBackground(new Color(30, 33, 42));
            card.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

            JLabel subtitle = new JLabel("Voce sabe o que e pong", SwingConstants.CENTER);
            subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            subtitle.setForeground(new Color(180, 180, 180));
            subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton startBtn = createMenuButton("Start Game");
            JButton exitBtn = createMenuButton("Exit");

            startBtn.addActionListener(e -> launchGame(menuFrame));
            exitBtn.addActionListener(e -> System.exit(0));

            card.add(subtitle);
            card.add(Box.createRigidArea(new Dimension(0, 24)));
            card.add(startBtn);
            card.add(Box.createRigidArea(new Dimension(0, 12)));
            card.add(exitBtn);

            menuFrame.add(title, BorderLayout.NORTH);
            menuFrame.add(card, BorderLayout.CENTER);

            menuFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            menuFrame.setLocationRelativeTo(null);
            menuFrame.setVisible(true);
        });
    }

    private static JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(220, 48));
        button.setMaximumSize(new Dimension(240, 48));
        button.setBackground(new Color(98, 210, 255));
        button.setForeground(new Color(18, 18, 22));
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 180, 225)),
                BorderFactory.createEmptyBorder(10, 24, 10, 24)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(118, 230, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(98, 210, 255));
            }
        });
        return button;
    }

    private static void installLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
        }
    }

    private static void launchGame(JFrame menuFrame) {
        JFrame gameFrame = new JFrame("Pong");
        gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameFrame.getContentPane().setBackground(new Color(18, 18, 18));

        PongGame game = new PongGame();
        game.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

        gameFrame.add(game);
        gameFrame.pack();
        gameFrame.setResizable(false);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);

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
