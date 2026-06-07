//Abstract class that will incorporate all powers
//Maybe separete by ballPowers and paddlePowers?
//Il need a last touched variable(just make a variable that changes from 0 user to 1 pc)
//everytime a paddle ball collision happens.

import java.awt.*;

public abstract class Powers implements powerMethods{

    public String getPowerSource() {
        return "generic power";
    }

    private static boolean blockActive = false;
    private static int bx, by, bw, bh;
    private static int powerType = -1;
    private static Color bcolor = new Color(255, 255, 143);

    private static String debugText = null;
    private static int debugTimer = 0;

    public static boolean hasActiveBlock() {
        return blockActive;
    }

    public static void spawnRandom(int windowWidth, int windowHeight) {
        spawnType(windowWidth, windowHeight, (int) (Math.random() * 4));
    }

    public static void spawnSpeedPowerup(int windowWidth, int windowHeight) {
        spawnType(windowWidth, windowHeight, 0);
    }

    private static void spawnType(int windowWidth, int windowHeight, int type) {
        if (blockActive) return;
        bw = 24;
        bh = 124;
        bx = 50 + (int) (Math.random() * (windowWidth - 100 - bw));
        by = 20 + (int) (Math.random() * (windowHeight - 40 - bh));
        powerType = type;
        blockActive = true;
        updateBlockColor();
    }

    private static void updateBlockColor() {
        switch (powerType) {
            case 0:
                bcolor = new Color(102, 204, 255); // blue because sonic speed ball block
                break;
            case 1:
                bcolor = new Color(102, 255, 102); // green speed paddle block
                break;
            case 2:
                bcolor = new Color(255, 204, 102); // orange size ball block
                break;
            case 3:
                bcolor = new Color(255, 102, 204); // pink size paddle block
                break;
            default:
                bcolor = new Color(255, 255, 143);
        }
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

    public static void paintDebugText(Graphics g) {
        if (debugTimer <= 0 || debugText == null) return;
        Color prev = g.getColor();
        Font prevFont = g.getFont();
        g.setFont(new Font("Segoe UI", Font.BOLD, 9));
        g.setColor(Color.WHITE);
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(debugText);
        int x = (bx + bw / 2) - (textWidth / 2);
        int y = by - 10;
        if (y < 30) {
            y = by + bh + 24;
        }
        g.drawString(debugText, x, y);
        g.setColor(prev);
        g.setFont(prevFont);
        debugTimer--;
        if (debugTimer <= 0) {
            debugText = null;
        }
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
        switch (powerType) {
            case 0:
                b.speedPowerup();
                debugText = "Ball speed powerup activated";
                break;
            case 1:
                if (lastTouched == 0) {
                    user.speedPowerup();
                    debugText = "User paddle speed powerup";
                } else {
                    pc.speedPowerup();
                    debugText = "PC paddle speed powerup";
                }
                break;
            case 2:
                b.sizePowerup();
                debugText = "Ball size powerup";
                break;
            case 3:
                if (lastTouched == 0) {
                    user.sizePowerup();
                    debugText = "User paddle size powerup";
                } else {
                    pc.sizePowerup();
                    debugText = "PC paddle size powerup";
                }
                break;
            default:
                int which = (int) (Math.random() * 4);
                switch (which) {
                    case 0:
                        b.speedPowerup();
                        debugText = "Ball speed powerup activated";
                        break;
                    case 1:
                        if (lastTouched == 0) {
                            user.speedPowerup();
                            debugText = "User paddle speed powerup";
                        } else {
                            pc.speedPowerup();
                            debugText = "PC paddle speed powerup";
                        }
                        break;
                    case 2:
                        b.sizePowerup();
                        debugText = "Ball size powerup";
                        break;
                    case 3:
                        if (lastTouched == 0) {
                            user.sizePowerup();
                            debugText = "User paddle size powerup";
                        } else {
                            pc.sizePowerup();
                            debugText = "PC paddle size powerup";
                        }
                        break;
                }
        }

        debugTimer = 30;
        blockActive = false;
        powerType = -1;
    }

}
