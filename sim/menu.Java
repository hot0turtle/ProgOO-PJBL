import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class SwingMenuExample {
    private static final int WINDOW_WIDTH = PongGame.WINDOW_WIDTH;
    private static final int WINDOW_HEIGHT = PongGame.WINDOW_HEIGHT;

    private static String currentPlayer = "AAA";
    private static JLabel titleLabel;
    private static DefaultListModel<String> leaderboardModel = new DefaultListModel<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SwingMenuExample::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        installLookAndFeel();

        JFrame frame = new JFrame("Game Menu Example");
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
        JButton settingsBtn = createMenuButton("Settings");
        JButton aboutBtn = createMenuButton("About");
        JButton exitBtn = createMenuButton("Exit");

        startBtn.addActionListener(e -> {
            String name = showNamePicker(frame, currentPlayer);
            if (name != null) {
                currentPlayer = name;
                titleLabel.setText(getTitleText());
                launchGame(frame);
            }
        });
        settingsBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "No settings yet."));
        aboutBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Simple Swing menu example."));
        exitBtn.addActionListener(e -> System.exit(0));

        card.add(subtitle);
        card.add(Box.createRigidArea(new Dimension(0, 18)));
        card.add(startBtn);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(settingsBtn);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(aboutBtn);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
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
        return "Pong Launcher - Player: " + currentPlayer;
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

            final int idx = i;
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
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        btns.add(ok);
        btns.add(cancel);
        root.add(btns);

        ok.addActionListener(a -> {
            StringBuilder sb = new StringBuilder();
            for (JLabel l : labels) sb.append(l.getText().charAt(0));
            result[0] = sb.toString();
            dialog.dispose();
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
        button.setMaximumSize(new Dimension(260, 48));
        button.setBackground(new Color(98, 210, 255));
        button.setForeground(new Color(18, 18, 22));
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 180, 225)),
                BorderFactory.createEmptyBorder(10, 18, 10, 18)
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
        JFrame gameFrame = new JFrame("Pong Game");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.getContentPane().setBackground(new Color(18, 18, 18));

        PongGame game = new PongGame();
        game.setPreferredSize(new Dimension(PongGame.WINDOW_WIDTH, PongGame.WINDOW_HEIGHT));

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
