import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PongGame extends JPanel implements MouseMotionListener, KeyListener {

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

    public PongGame(String playerName, Runnable onReturnToMenu) {
        this.playerName = playerName;
        this.onReturnToMenu = onReturnToMenu;

        gameBall = new Ball(
                300,
                200,
                3,
                3,
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
                2.5f,
                Color.RED,
                this
        );

        userMouseY = 0;

        userScore = 0;
        pcScore = 0;

        bounceCount = 0;

        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        addMouseMotionListener(this);
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

    }

    // Handles all game updates
    public void gameLogic() {

        // move ball
        gameBall.moveBall();

        // bounce top/bottom
        gameBall.bounceOffEdges(0, getHeight());

        // move player paddle
        userPaddle.moveTowards(userMouseY);

        // move pc paddle
        pcPaddle.moveTowards(gameBall.getY());

        // collision detection
        if (userPaddle.checkCollision(gameBall) || pcPaddle.checkCollision(gameBall)) {

            gameBall.reverseX();

            bounceCount++;

        }

        // increase difficulty
        if (bounceCount == 5) {

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

        gameBall.setCx(3);
        gameBall.setCy(3);

        gameBall.setSpeed(3);

        bounceCount = 0;

    }

    public int getUserScore() {
        return userScore;
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
}