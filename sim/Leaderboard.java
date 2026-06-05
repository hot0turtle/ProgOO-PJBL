//I need to save the ball object to get the speed so that I could do a continue from hihgscore mechanic
//should make the nickname ui not look bad but cant look at this anymore
import java.io.*;
import java.util.*;
import java.awt.Color;

public class Leaderboard {

    public static class Entry {
        public final String name;
        public final int score;
        public final Ball ball;
        public final PaddleData userPaddleData;
        public final PaddleData pcPaddleData;

        public Entry(String name, int score, Ball ball, PaddleData userPaddleData, PaddleData pcPaddleData) {
            this.name = name;
            this.score = score;
            this.ball = ball;
            this.userPaddleData = userPaddleData;
            this.pcPaddleData = pcPaddleData;
        }

        // Constructor for backward compatibility
        public Entry(String name, int score, Ball ball) {
            this(name, score, ball, null, null);
        }

        @Override
        public String toString() {
            return String.format("%s - %d - %d", name, score, ball.getSpeed());
        }
    }

    public static class PaddleData {
        public final int x;
        public final int y;
        public final int height;
        public final float speed;
        public final int color;

        public PaddleData(int x, int y, int height, float speed, int color) {
            this.x = x;
            this.y = y;
            this.height = height;
            this.speed = speed;
            this.color = color;
        }

        public String serialize() {
            return x + ":" + y + ":" + height + ":" + speed + ":" + color;
        }

        public static PaddleData deserialize(String data) {
            String[] parts = data.split(":");
            if (parts.length >= 5) {
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                int height = Integer.parseInt(parts[2]);
                float speed = Float.parseFloat(parts[3]);
                int color = Integer.parseInt(parts[4]);
                return new PaddleData(x, y, height, speed, color);
            }
            return null;
        }
    }

    private static final File FILE = new File("leaderBoard.txt");

    public static List<Entry> load() {
        List<Entry> list = new ArrayList<>();
        if (!FILE.exists()) return list;
        try (BufferedReader r = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String name = parts[0];
                    int score = 0;
                    try { score = Integer.parseInt(parts[1]); } catch (Exception ignored) {}
                    
                    // Parse the ball
                    String[] ballParts = parts[2].split("\\|");
                    if (ballParts.length >= 5) {
                        int x = Integer.parseInt(ballParts[0]);
                        int y = Integer.parseInt(ballParts[1]);
                        int cx = Integer.parseInt(ballParts[2]);
                        int cy = Integer.parseInt(ballParts[3]);
                        int speed = Integer.parseInt(ballParts[4]);

                        Ball ball = new Ball(x, y, cx, cy, speed, Color.WHITE, 10);
                        
                        // Parse paddle data if available
                        PaddleData userPaddleData = null;
                        PaddleData pcPaddleData = null;
                        if (parts.length >= 5) {
                            userPaddleData = PaddleData.deserialize(parts[3]);
                            pcPaddleData = PaddleData.deserialize(parts[4]);
                        }
                        
                        list.add(new Entry(name, score, ball, userPaddleData, pcPaddleData));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        list.sort((a,b)->Integer.compare(b.score,a.score));
        return list;
    }

    public static void save(List<Entry> list) {
        try (PrintWriter w = new PrintWriter(new FileWriter(FILE))) {
            for (Entry e : list) {
                Ball b = e.ball;
                String ballData = b.getX() + "|" + b.getY() + "|" + b.getCx() + "|" + b.getCy() + "|" 
                    + b.getSpeed() + "|" + b.getColor().getRGB() + "|" + b.getSize();
                
                String userPaddleStr = e.userPaddleData != null ? e.userPaddleData.serialize() : "0:200:75:3.0:-16776961";
                String pcPaddleStr = e.pcPaddleData != null ? e.pcPaddleData.serialize() : "610:200:75:2.0:-65536";
                
                w.println(e.name + "," + e.score + "," + ballData + "," + userPaddleStr + "," + pcPaddleStr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addEntry(String name, int score, Ball ball, Leaderboard.PaddleData userPaddle, Leaderboard.PaddleData pcPaddle) {
        List<Entry> list = load();
        list.add(new Entry(name, score, ball, userPaddle, pcPaddle));
        list.sort((a,b)->Integer.compare(b.score,a.score));
        // keep top 50
        if (list.size() > 50) list = list.subList(0,50);
        save(list);
    }

    public static Entry findByName(String name) {
        List<Entry> list = load();
        for (Entry e : list) {
            if (e.name.equals(name)) {
                return e;
            }
        }
        return null;
    }
}
