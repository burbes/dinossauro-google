import javax.swing.*;

public class GameWindow  extends JFrame {

    public static final int SCREEN_WIDTH = 1200;
    public static final int SCREEN_HEIGHT = 500;


    private GameScreen gameScreen;

    public GameWindow() {
        super("Java T-Rex game");
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
//        setLocation(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        gameScreen = new GameScreen();
        addKeyListener(gameScreen); //necessario add nesse metodo para funcionar teclado
        add(gameScreen);
    }

    public void startGame() {
        setVisible(true);
        gameScreen.startGame();
    }


    public static void main(String args[]) {
        (new GameWindow()).startGame();
    }


}
