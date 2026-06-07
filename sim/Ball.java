import java.awt.*;

public class Ball extends Powers {

    private int x;
    private int y;

    private float cx;
    private float cy;

    private float speed;

    private int size;

    private Color color;

    static final float MAX_SPEED = 15f;

    public Ball(
            int x,
            int y,
            float cx,
            float cy,
            float speed,
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
            speed = speed + 0.25f;

            if (cx > 0) {
                cx = speed;

            } else {
                cx = -speed;

            }

            if (cy > 0) {
                cy = speed;

            } else {
                cy = -speed;

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

    public float getCx() {
        return cx;
    }

    public float getCy() {
        return cy;
    }

    public float getSpeed() {
        return speed;
    }

    public Color getColor() {
        return color;
    }

    // Setters

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setCx(float cx) {
        this.cx = cx;
    }

    public void setCy(float cy) {
        this.cy = cy;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setSize(int size){
        this.size = size;
    }

    @Override
    public String getPowerSource() {
        return "ball power";
    }

    @Override
    public void sizePowerup(){
        size += size / 2;

    }

    @Override
    public void speedPowerup(){
        //reusing the speedup code but with different values
        if (speed < MAX_SPEED) {
            speed = speed + 1.75f;

            if (cx > 0) {
                cx = speed;

            } else {
                cx = -speed;

            }

            if (cy > 0) {
                cy = speed;

            } else {
                cy = -speed;

            }

        }
    }

}