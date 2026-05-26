import java.awt.*;

public class Ball {

    private int x;
    private int y;

    private int cx;
    private int cy;

    private int speed;

    private int size;

    private Color color;

    static final int MAX_SPEED = 7;

    public Ball(
            int x,
            int y,
            int cx,
            int cy,
            int speed,
            Color color,
            int size
    ) {

        this.x = x;
        this.y = y;

        this.cx = cx;
        this.cy = cy;

        this.speed = speed;

        this.color = color;

        this.size = size;

    }

    //Draw ball
    public void paint(Graphics g) {

        g.setColor(color);

        g.fillOval(x, y, size, size);

    }

    //Move ball one frame
    public void moveBall() {

        x += cx;
        y += cy;

    }

    //Bounce off top/bottom edges
    public void bounceOffEdges(int top, int bottom) {

        // bottom
        if (y > bottom - size) {
            reverseY();

        }
        // top
        else if (y < top) {
            reverseY();

        }

    }

    // Reverse X direction
    public void reverseX() {
        cx *= -1;

    }

    //Reverse Y direction
    public void reverseY() {
        cy *= -1;

    }

    // Increase ball speed
    public void increaseSpeed() {

        if (speed < MAX_SPEED) {
            speed++;

            if (cx > 0) {
                cx = speed;

            } else {
                cx = speed * -1;

            }

            if (cy > 0) {
                cy = speed;

            } else {
                cy = speed * -1;

            }

        }

    }

    // Getters

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return size;
    }

    // Setters

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setCx(int cx) {
        this.cx = cx;
    }

    public void setCy(int cy) {
        this.cy = cy;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}