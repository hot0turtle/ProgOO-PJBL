import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    static JFrame f = new JFrame("Pong");

    public static void main(String[] args) {

        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        PongGame game = new PongGame();
        game.setPreferredSize(new Dimension(PongGame.WINDOW_WIDTH, PongGame.WINDOW_HEIGHT));

        f.add(game);
        f.pack();
        f.setResizable(false);
        f.setVisible(true);

        Timer timer = new Timer(33, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                game.gameLogic();

                game.repaint();

            }
        });

        timer.start();

    }
}