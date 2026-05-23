import javax.swing.*;

public class other {

	//declare and initialize the frame
    static JFrame f = new JFrame("Pong");

    public static void main(String[] args) {

		//make it so program exits on close button click
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		//the size of the game will be 480x640, the size of the JFrame needs to be slightly larger
        f.setSize(650,495);


        game Game = new game();

        //add the game to the JFrame
        f.add(Game);
		//show the window
        f.setVisible(true);

	}
}