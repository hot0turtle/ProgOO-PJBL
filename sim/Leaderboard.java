//I need to save the ball object to get the speed so that I could do a continue from hihgscore mechanic
//should make the nickname ui not look bad but cant look at this anymore
import java.io.*;
import java.util.*;

public class Leaderboard {

    public static class Entry {
        public final String name;
        public final int score;

        public Entry(String name, int score) {
            this.name = name;
            this.score = score;
        }

        @Override
        public String toString() {
            return String.format("%s - %d", name, score);
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
                if (parts.length >= 2) {
                    String name = parts[0];
                    int score = 0;
                    try { score = Integer.parseInt(parts[1]); } catch (Exception ignored) {}
                    list.add(new Entry(name, score));
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
                w.println(e.name + "," + e.score);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addEntry(String name, int score) {
        List<Entry> list = load();
        list.add(new Entry(name, score));
        list.sort((a,b)->Integer.compare(b.score,a.score));
        // keep top 50
        if (list.size() > 50) list = list.subList(0,50);
        save(list);
    }
}
