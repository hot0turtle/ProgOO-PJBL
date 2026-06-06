import java.awt.*;
import javax.swing.JPanel;

public class Paddle extends Powers {

    private int height;
    private int x;
    private int y;
    private float speed;
    private Color color;
    private JPanel panel;


    static final int PADDLE_WIDTH = 15;

    public Paddle(
            int x,
            int y,
            int height,
            float speed,
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

        int ballSize = b.getSize();
        int rightX = x + PADDLE_WIDTH;
        int bottomY = y + height;

        // Ball coordinates represent top-left corner of its bounding box
        int ballLeft = b.getX();
        int ballRight = b.getX() + ballSize;
        int ballTop = b.getY();
        int ballBottom = b.getY() + ballSize;

        // AABB (Axis-Aligned Bounding Box) collision detection
        if (ballRight > x && ballLeft < rightX &&
            ballBottom > y && ballTop < bottomY) {

            return true;

        }

        return false;

    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getHeight() {
        return height;
    }

    public float getSpeed() {
        return speed;
    }

    public Color getColor() {
        return color;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public void sizePowerup(){
        height++;
    }

    @Override
    public void speedPowerup(){
        speed++;
    }
}