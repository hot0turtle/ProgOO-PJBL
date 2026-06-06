//Abstract class that will incorporate all powers
//Maybe separete by ballPowers and paddlePowers?
//Il need a last touched variable(just make a variable that changes from 0 user to 1 pc)
//everytime a paddle ball collision happens.

import java.awt.*;

public abstract class Powers{

    public abstract void speedPowerup();

    public abstract void sizePowerup();


    private static boolean blockActive = false;
    private static int bx, by, bw, bh;
    private static Color bcolor = new Color(255, 200, 0);

    public static boolean hasActiveBlock() {
        return blockActive;
    }

    public static void spawnRandom(int windowWidth, int windowHeight) {
        if (blockActive) return;
        bw = 24;
        bh = 124;
        bx = 50 + (int) (Math.random() * (windowWidth - 100 - bw));
        by = 20 + (int) (Math.random() * (windowHeight - 40 - bh));
        blockActive = true;
    }

    public static void paintBlock(Graphics g) {
        if (!blockActive) return;
        Color prev = g.getColor();
        g.setColor(bcolor);
        g.fillRect(bx, by, bw, bh);
        g.setColor(Color.BLACK);
        g.drawRect(bx, by, bw, bh);
        g.setColor(prev);
    }

    public static boolean intersectsBall(Ball b) {
        if (!blockActive) return false;
        int ballLeft = b.getX();
        int ballTop = b.getY();
        int ballSize = b.getSize();
        int ballRight = ballLeft + ballSize;
        int ballBottom = ballTop + ballSize;

        int blockLeft = bx;
        int blockRight = bx + bw;
        int blockTop = by;
        int blockBottom = by + bh;

        return ballRight > blockLeft && ballLeft < blockRight && ballBottom > blockTop && ballTop < blockBottom;
    }

    public static void activateIfCollides(Ball b, Paddle user, Paddle pc, int lastTouched) {
        if (!intersectsBall(b)) return;

        int which = (int) (Math.random() * 4);
        switch (which) {
            case 0:
                b.speedPowerup();
                break;
            case 1:
                if (lastTouched == 0) user.speedPowerup(); else pc.speedPowerup();
                break;
            case 2:
                b.sizePowerup();
                break;
            case 3:
                if (lastTouched == 0) user.sizePowerup(); else pc.sizePowerup();
                break;
        }

        blockActive = false;
    }

}
