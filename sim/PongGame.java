import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PongGame extends JPanel implements MouseMotionListener {

    static final int WINDOW_WIDTH = 640;
    static final int WINDOW_HEIGHT = 480;

    private Ball gameBall;
    private Paddle userPaddle;
    private Paddle pcPaddle;

    private int userMouseY;

    private int userScore;
    private int pcScore;

    private int bounceCount;

    public PongGame() {

        gameBall = new Ball(
                300,
                200,
                3,
                3,
                3,
                Color.YELLOW,
                10
        );

        userPaddle = new Paddle(
                10,
                200,
                75,
                3,
                Color.BLUE,
                this
                
        );

        pcPaddle = new Paddle(
                610,
                200,
                75,
                3,
                Color.RED,
                this
        );

        userMouseY = 0;

        userScore = 0;
        pcScore = 0;

        bounceCount = 0;

        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        addMouseMotionListener(this);

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
                "Score - User [ " + userScore + " ]   PC [ " + pcScore + " ]",
                230,
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


    @Override
    public void mouseMoved(MouseEvent e) {

        userMouseY = e.getY();

    }
}