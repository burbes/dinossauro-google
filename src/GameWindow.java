import javax.swing.JFrame;

public class GameWindow extends JFrame {

    public static final int SCREEN_WIDTH = 1200;
    public static final int SCREEN_HEIGHT = 500;

    private GameScreen gameScreen;

    public GameWindow() {
        super("Java T-Rex Game");
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        gameScreen = new GameScreen();
        addKeyListener(gameScreen);
        add(gameScreen);
    }

    public void startGame() {
        setVisible(true);
        gameScreen.startGame();
    }

    public static void main(String[] args) {
        new GameWindow().startGame();
    }
}
