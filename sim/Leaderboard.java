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

        public Entry(String name, int score, Ball ball) {
            this.name = name;
            this.score = score;
            this.ball = ball;
        }

        @Override
        public String toString() {
            return String.format("%s - %d - %d", name, score, ball.getSpeed());
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
                    
                    // Parse the ball - only load speed, use defaults for rest
                    String[] ballParts = parts[2].split("\\|");
                    if (ballParts.length >= 5) {
                        int speed = Integer.parseInt(ballParts[4]);
                        // Create ball with default values but preserve speed
                        Ball ball = new Ball(400, 300, 5, 5, speed, Color.WHITE, 10);
                        list.add(new Entry(name, score, ball));
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
                w.println(e.name + "," + e.score + "," + ballData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addEntry(String name, int score, Ball ball) {
        List<Entry> list = load();
        list.add(new Entry(name, score, ball));
        list.sort((a,b)->Integer.compare(b.score,a.score));
        // keep top 50
        if (list.size() > 50) list = list.subList(0,50);
        save(list);
    }
}
