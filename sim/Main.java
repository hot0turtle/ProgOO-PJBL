import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class Main {
    private static final int WINDOW_WIDTH = PongGame.WINDOW_WIDTH;
    private static final int WINDOW_HEIGHT = PongGame.WINDOW_HEIGHT;

    private static String currentPlayer = "AAA";
    private static JLabel titleLabel;
    private static DefaultListModel<String> leaderboardModel = new DefaultListModel<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        installLookAndFeel();

        JFrame frame = new JFrame("Pong");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(20, 22, 30));
        frame.setLayout(new BorderLayout());

        titleLabel = new JLabel(getTitleText(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(24, 0, 10, 0));

        JPanel card = new JPanel();
        card.setOpaque(true);
        card.setBackground(new Color(34, 37, 49));
        card.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel subtitle = new JLabel("Ready to play?", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle.setForeground(new Color(180, 180, 180));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startBtn = createMenuButton("Start");
        JButton exitBtn = createMenuButton("Exit");

        startBtn.addActionListener(e -> {
            String name = showNamePicker(frame, currentPlayer);
            if (name != null) {
                currentPlayer = name;
                titleLabel.setText(getTitleText());
                launchGame(frame);
            }
        });
        exitBtn.addActionListener(e -> System.exit(0));

        card.add(subtitle);
        card.add(Box.createRigidArea(new Dimension(0, 18)));
        card.add(startBtn);
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(exitBtn);

        // Leaderboard panel on the right
        JPanel lbPanel = new JPanel();
        lbPanel.setPreferredSize(new Dimension(220, WINDOW_HEIGHT));
        lbPanel.setLayout(new BorderLayout());
        lbPanel.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        lbPanel.setBackground(new Color(28,30,38));

        JLabel lbTitle = new JLabel("Leaderboard", SwingConstants.CENTER);
        lbTitle.setForeground(Color.WHITE);
        lbTitle.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));
        lbPanel.add(lbTitle, BorderLayout.NORTH);

        JList<String> lbList = new JList<>(leaderboardModel);
        lbList.setBackground(new Color(34,37,49));
        lbList.setForeground(Color.WHITE);
        lbPanel.add(new JScrollPane(lbList), BorderLayout.CENTER);


        frame.add(titleLabel, BorderLayout.NORTH);
        frame.add(card, BorderLayout.CENTER);
        frame.add(lbPanel, BorderLayout.EAST);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        refreshLeaderboard();
    }

    private static String getTitleText() {
        return "Pong - Player: " + currentPlayer;
    }

    private static void refreshLeaderboard() {
        leaderboardModel.clear();
        List<Leaderboard.Entry> entries = Leaderboard.load();
        for (Leaderboard.Entry e : entries) {
            leaderboardModel.addElement(e.toString());
        }
    }

    private static String showNamePicker(JFrame parent, String initial) {
        final String[] result = {null};
        JDialog dialog = new JDialog(parent, "Enter Your Initials", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JPanel lettersPanel = new JPanel(new FlowLayout());
        lettersPanel.setOpaque(false);
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ ";
        char[] init = (initial == null ? "AAA" : initial).toUpperCase().toCharArray();
        JLabel[] labels = new JLabel[3];

        for (int i = 0; i < 3; i++) {
            JPanel col = new JPanel();
            col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
            col.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));

            JButton up = new JButton("▲");
            JLabel lbl = new JLabel(String.valueOf(init[Math.min(i, init.length-1)]), SwingConstants.CENTER);
            lbl.setFont(new Font("Monospaced", Font.BOLD, 28));
            lbl.setBorder(BorderFactory.createEmptyBorder(6,12,6,12));
            JButton down = new JButton("▼");

            up.addActionListener(a -> {
                char c = lbl.getText().charAt(0);
                int p = chars.indexOf(c);
                p = (p + 1) % chars.length();
                lbl.setText(String.valueOf(chars.charAt(p)));
            });
            down.addActionListener(a -> {
                char c = lbl.getText().charAt(0);
                int p = chars.indexOf(c);
                p = (p - 1 + chars.length()) % chars.length();
                lbl.setText(String.valueOf(chars.charAt(p)));
            });

            col.add(up);
            col.add(lbl);
            col.add(down);
            lettersPanel.add(col);
            labels[i] = lbl;
        }

        root.add(lettersPanel);

        JPanel btns = new JPanel(new FlowLayout());
        JButton ok = createMenuButton("OK");
        JButton cancel = createMenuButton("Cancel");
        JButton continueButton = createMenuButton("Continue");
        btns.add(ok);
        btns.add(continueButton);
        btns.add(cancel);
        root.add(btns);

        ok.addActionListener(a -> {
            StringBuilder sb = new StringBuilder();
            for (JLabel l : labels) sb.append(l.getText().charAt(0));
            result[0] = sb.toString();
            dialog.dispose();
        });

        continueButton.addActionListener(a -> {
            StringBuilder sb = new StringBuilder();
            for (JLabel l : labels) sb.append(l.getText().charAt(0));
            String name = sb.toString();
            
            // Try to load saved game
            Leaderboard.Entry entry = Leaderboard.findByName(name);
            if (entry != null && entry.ball != null) {
                dialog.dispose();
                // Launch game with restored state
                launchGameFromSave(parent, name, entry.ball, entry.userPaddleData, entry.pcPaddleData);
            } else {
                JOptionPane.showMessageDialog(dialog, "No save found for " + name);
            }
        });       

        cancel.addActionListener(a -> {
            result[0] = null;
            dialog.dispose();
        });

        dialog.getContentPane().add(root);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
        return result[0];
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

        final Timer[] timerRef = {null};
        final PongGame[] gameRef = {null};

        PongGame game = new PongGame(currentPlayer, () -> {
            // Return to menu callback: save score and return to menu
            if (timerRef[0] != null) {
                timerRef[0].stop();
            }
            int finalScore = gameRef[0].getUserScore();
            if (finalScore > 0) {
                Paddle userPaddle = gameRef[0].getUserPaddle();
                Paddle pcPaddle = gameRef[0].getPCPaddle();
                Leaderboard.PaddleData userPaddleData = new Leaderboard.PaddleData(
                        userPaddle.getX(), userPaddle.getY(), userPaddle.getHeight(), 
                        userPaddle.getSpeed(), userPaddle.getColor().getRGB()
                );
                Leaderboard.PaddleData pcPaddleData = new Leaderboard.PaddleData(
                        pcPaddle.getX(), pcPaddle.getY(), pcPaddle.getHeight(),
                        pcPaddle.getSpeed(), pcPaddle.getColor().getRGB()
                );
                Leaderboard.addEntry(currentPlayer, finalScore, gameRef[0].getBall(), userPaddleData, pcPaddleData);
            }
            gameFrame.dispose();
            SwingUtilities.invokeLater(Main::createAndShowGUI);
        });

        gameRef[0] = game;
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

        timerRef[0] = timer;
        timer.start();
        game.requestFocus();
        
        menuFrame.dispose();
    }

    private static void launchGameFromSave(JFrame menuFrame, String playerName, Ball savedBall, 
                                           Leaderboard.PaddleData userPaddleData, Leaderboard.PaddleData pcPaddleData) {
        currentPlayer = playerName;
        
        JFrame gameFrame = new JFrame("Pong");
        gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameFrame.getContentPane().setBackground(new Color(18, 18, 18));

        final Timer[] timerRef = {null};
        final PongGame[] gameRef = {null};

        PongGame game = new PongGame(playerName, () -> {
            // Return to menu callback: save score and return to menu
            if (timerRef[0] != null) {
                timerRef[0].stop();
            }
            int finalScore = gameRef[0].getUserScore();
            if (finalScore > 0) {
                Paddle userPaddle = gameRef[0].getUserPaddle();
                Paddle pcPaddle = gameRef[0].getPCPaddle();
                Leaderboard.PaddleData newUserPaddleData = new Leaderboard.PaddleData(
                        userPaddle.getX(), userPaddle.getY(), userPaddle.getHeight(),
                        userPaddle.getSpeed(), userPaddle.getColor().getRGB()
                );
                Leaderboard.PaddleData newPcPaddleData = new Leaderboard.PaddleData(
                        pcPaddle.getX(), pcPaddle.getY(), pcPaddle.getHeight(),
                        pcPaddle.getSpeed(), pcPaddle.getColor().getRGB()
                );
                Leaderboard.addEntry(playerName, finalScore, gameRef[0].getBall(), newUserPaddleData, newPcPaddleData);
            }
            gameFrame.dispose();
            SwingUtilities.invokeLater(Main::createAndShowGUI);
        }, savedBall, userPaddleData, pcPaddleData);

        gameRef[0] = game;
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

        timerRef[0] = timer;
        timer.start();
        game.requestFocus();
        
        menuFrame.dispose();
    }
}
