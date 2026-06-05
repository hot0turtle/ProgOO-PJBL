//I need to override pongGame for the continue, instead of setting the default I set whatever I saved in the file and do it with that instead

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PongGame extends JPanel implements MouseMotionListener, KeyListener, MouseListener {

    static final int WINDOW_WIDTH = 640;
    static final int WINDOW_HEIGHT = 480;

    private Ball gameBall;
    private Paddle userPaddle;
    private Paddle pcPaddle;

    private int userMouseY;

    private int userScore;
    private int pcScore;

    private int bounceCount;

    private String playerName;
    private Runnable onReturnToMenu;
    private boolean isPaused = false;

    public PongGame(String playerName, Runnable onReturnToMenu) {
        this.playerName = playerName;
        this.onReturnToMenu = onReturnToMenu;

        int startCx = Math.random() < 0.5 ? 3 : -3;
        int startCy = Math.random() < 0.5 ? 3 : -3;
        gameBall = new Ball(
                300,
                200,
                startCx,
                startCy,
                6,
                Color.YELLOW,
                10
        );

        userPaddle = new Paddle(
                10,
                200,
                75,
                3f,
                Color.BLUE,
                this
        );

        pcPaddle = new Paddle(
                610,
                200,
                75,
                5f,
                Color.RED,
                this
        );

        userMouseY = 0;

        userScore = 0;
        pcScore = 0;

        bounceCount = 0;

        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        addMouseMotionListener(this);
        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true);

    }

    // Constructor for resuming from saved state
    public PongGame(String playerName, Runnable onReturnToMenu, Ball savedBall, 
                    Leaderboard.PaddleData savedUserPaddle, Leaderboard.PaddleData savedPCPaddle) {
        this.playerName = playerName;
        this.onReturnToMenu = onReturnToMenu;

        gameBall = savedBall != null ? savedBall : new Ball(300, 200, 3, 3, 6, Color.YELLOW, 10);

        if (savedUserPaddle != null) {
            userPaddle = new Paddle(
                    savedUserPaddle.x,
                    savedUserPaddle.y,
                    savedUserPaddle.height,
                    savedUserPaddle.speed,
                    new Color(savedUserPaddle.color),
                    this
            );
        } else {
            userPaddle = new Paddle(10, 200, 75, 3f, Color.BLUE, this);
        }

        if (savedPCPaddle != null) {
            pcPaddle = new Paddle(
                    savedPCPaddle.x,
                    savedPCPaddle.y,
                    savedPCPaddle.height,
                    savedPCPaddle.speed,
                    new Color(savedPCPaddle.color),
                    this
            );
        } else {
            pcPaddle = new Paddle(610, 200, 75, 2f, Color.RED, this);
        }

        userMouseY = 0;

        userScore = 0;
        pcScore = 0;

        bounceCount = 0;

        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        addMouseMotionListener(this);
        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true);
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        // background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        // ball
        gameBall.paint(g);

        // paddles
        userPaddle.paint(g);
        pcPaddle.paint(g);

        // score
        g.setColor(Color.WHITE);

        g.drawString(
                playerName + " [ " + userScore + " ]   PC [ " + pcScore + " ]",
                200,
                20
        );

        // paused indicator
        if (isPaused) {
            g.setColor(new Color(255, 255, 255, 150));
            g.setFont(new Font("Segoe UI", Font.BOLD, 48));
            String pausedText = "PAUSED";
            FontMetrics fm = g.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(pausedText)) / 2;
            int y = (getHeight() + fm.getAscent()) / 2;
            g.drawString(pausedText, x, y);
        }

    }

    // Handles all game updates
    public void gameLogic() {

        if (isPaused) return;

        // move ball
        gameBall.moveBall();

        // bounce top/bottom
        gameBall.bounceOffEdges(0, getHeight());

        // move player paddle
        userPaddle.moveTowards(userMouseY);

        // move pc paddle
        pcPaddle.moveTowards(gameBall.getY());

        // collision detection
        boolean userHit = userPaddle.checkCollision(gameBall);
        boolean pcHit = pcPaddle.checkCollision(gameBall);
        if (userHit || pcHit) {
            Paddle hitPaddle = userHit ? userPaddle : pcPaddle;
            int currentCx = gameBall.getCx();
            boolean movingIntoPaddle = (userHit && currentCx < 0) || (pcHit && currentCx > 0);

            if (movingIntoPaddle) {
                int paddleCenterY = hitPaddle.getY() + hitPaddle.getHeight() / 2;
                int ballCenterY = gameBall.getY() + gameBall.getSize() / 2;
                float relativeIntersectY = (ballCenterY - paddleCenterY) / (hitPaddle.getHeight() / 2f);
                relativeIntersectY = Math.max(-1f, Math.min(1f, relativeIntersectY));

                int maxVerticalSpeed = 4;
                int newCy = Math.round(relativeIntersectY * maxVerticalSpeed);
                if (newCy == 0) {
                    newCy = gameBall.getCy() >= 0 ? 1 : -1;
                }
                gameBall.setCy(newCy);
                gameBall.reverseX();

                if (userHit) {
                    gameBall.setX(hitPaddle.getX() + Paddle.PADDLE_WIDTH);
                } else {
                    gameBall.setX(hitPaddle.getX() - gameBall.getSize());
                }

                bounceCount++;
            }
        }

        // increase difficulty
        if (bounceCount == 3) {

            bounceCount = 0;

            gameBall.increaseSpeed();

        }

        // scoring
        if (gameBall.getX() < 0) {

            pcScore++;

            reset();

        } else if (gameBall.getX() > WINDOW_WIDTH) {

            userScore++;

            reset();

        }

    }

    // Reset round
    public void reset() {

        try {

            Thread.sleep(1000);

        } catch (Exception e) {

            e.printStackTrace();

        }

        gameBall.setX(300);
        gameBall.setY(200);

        gameBall.setCx(Math.random() < 0.5 ? 3 : -3);
        gameBall.setCy(Math.random() < 0.5 ? 3 : -3);

        gameBall.setSpeed(3);

        bounceCount = 0;

    }

    public int getUserScore() {
        return userScore;
    }

    public Ball getBall() {
        return gameBall; 
    }

    public Paddle getUserPaddle() {
        return userPaddle;
    }

    public Paddle getPCPaddle() {
        return pcPaddle;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        userMouseY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        userMouseY = e.getY();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (onReturnToMenu != null) {
                onReturnToMenu.run();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        isPaused = false;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        isPaused = true;
        try {
            throw new MouseOutOfGameException("Mouse left game window - Game paused");
        } catch (MouseOutOfGameException ex) {
            // Exception caught - pausing game
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
}