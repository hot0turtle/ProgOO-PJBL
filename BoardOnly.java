import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BoardOnly extends JPanel implements KeyListener {
    private static final int COLUMNS = 30;
    private static final int ROWS = 30;
    private static final int BLOCK_SIZE = 30;
    private static final int BOARD_WIDTH = COLUMNS * BLOCK_SIZE;
    private static final int BOARD_HEIGHT = ROWS * BLOCK_SIZE;

    private final Color[][] board = new Color[ROWS][COLUMNS];

    public BoardOnly() {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        setBackground(Color.DARK_GRAY);
        setFocusable(true);
        addKeyListener(this);
        gameStart();
    }

    public void gameStart() {
        initializeBoard();
        playerX = COLUMNS / 2;
        playerY = ROWS / 2;
        repaint();
    }

    private void initializeBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                board[row][col] = null;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawPlayer(g);
    }

    private void drawBoard(Graphics g) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Color cellColor = board[row][col];
                g.setColor(cellColor == null ? Color.LIGHT_GRAY : cellColor);
                g.fillRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                g.setColor(Color.DARK_GRAY);
                g.drawRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Board Only");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(new BoardOnly());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private int playerX;
    private int playerY;
    private final Color playerColor = Color.PINK;

    private void movePiece(int dx, int dy) {
        if (!collides(playerX + dx, playerY + dy)) {
            playerX += dx;
            playerY += dy;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> movePiece(-1, 0);
            case KeyEvent.VK_RIGHT -> movePiece(1, 0);
            case KeyEvent.VK_UP -> movePiece(0, -1);
            case KeyEvent.VK_DOWN -> movePiece(0, 1);
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // NAO COMPILA SEM E EU N LEMBRO O PQ
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // NAO COMPILA SEM E EU N LEMBRO O PQ
    }

    private void drawPlayer(Graphics g) {
        g.setColor(playerColor);
        g.fillRect(playerX * BLOCK_SIZE, playerY * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        g.setColor(Color.BLACK);
        g.drawRect(playerX * BLOCK_SIZE, playerY * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
    }

    private boolean collides(int x, int y) {
        return x < 0 || x >= COLUMNS || y < 0 || y >= ROWS;
    }
}
