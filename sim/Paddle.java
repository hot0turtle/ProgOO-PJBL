import java.awt.*;
import javax.swing.JPanel;

public class Paddle {

    private int height;
    private int x;
    private int y;
    private int speed;
    private Color color;
    private JPanel panel;


    static final int PADDLE_WIDTH = 15;

    public Paddle(
            int x,
            int y,
            int height,
            int speed,
            Color color,
            JPanel panel
    ) {

        this.x = x;
        this.y = y;

        this.height = height;

        this.speed = speed;

        this.color = color;
        
        this.panel = panel;
    }

    // Draw paddle
    public void paint(Graphics g) {

        g.setColor(color);

        g.fillRect(x, y, PADDLE_WIDTH, height);

    }

    // Move paddle toward target Y
    public void moveTowards(int moveToY) {

        int frameHeight = panel.getHeight();

        int centerY = y + height / 2;

        if (Math.abs(centerY - moveToY) > speed) {

            if (centerY > moveToY) {
                if (y >= 0) 
                    y -= speed;

            }

            if (centerY < moveToY) {
                if (y + height <= frameHeight) 
                    y += speed;

            }

        }

    }

    // Collision detection
    public boolean checkCollision(Ball b) {

        int rightX = x + PADDLE_WIDTH;

        int bottomY = y + height;

        if (b.getX() > (x - b.getSize())
                && b.getX() < rightX) {

            if (b.getY() > y
                    && b.getY() < bottomY) {

                return true;

            }

        }

        return false;

    }
}