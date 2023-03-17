import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GameScreen extends JPanel implements Runnable, KeyListener {


    private static final int START_GAME_STATE = 0;
    private static final int GAME_PLAYING_STATE = 1;
    private static final int GAME_OVER_STATE = 2;

    public static final float GRAVITY = 0.1f;
    public static final float GROUNDy = 110; // by pixel

    private int score;

    ;
    private MainCharacter character;

    private List<MainCharacter> dinossaurs;
    private boolean isKeyPressed;

    private Thread thread;


    private int threadsCount = 2;

    private Clouds clouds;
    private Land land;
    private EnemiesManager enemiesManager;

    private int gameState = START_GAME_STATE;
    private BufferedImage gameOverButtonImage;

    @SuppressWarnings("unchecked")
    private AudioClip jumpSound;
    @SuppressWarnings("unchecked")
    private AudioClip deadSound;
    @SuppressWarnings("unchecked")
    private AudioClip scoreUpSound;


    public GameScreen() {
        thread = new Thread(this);
        gameOverButtonImage = Resource.getResourceImage("data/gameover_text.png");
        character = new MainCharacter();
        character.setX(50); //empurra o dino um pouco pra frente
        character.setY(60);
        dinossaurs = new ArrayList<>();
        dinossaurs.add(character);
        land = new Land(GameWindow.SCREEN_WIDTH);
        clouds = new Clouds();
        enemiesManager = new EnemiesManager(character, this);


        try {
            jumpSound = Applet.newAudioClip(new URL("file", "", "data/jump.wav"));
            deadSound = Applet.newAudioClip(new URL("file", "", "data/dead.wav"));
            scoreUpSound = Applet.newAudioClip(new URL("file", "", "data/scoreup.wav"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void startGame() {
        thread.start();
    }

    public void plusScore(int score) {
        this.score += score;
        scoreUpSound.play();
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.decode("#f7f7f7"));
        g.drawRect(0, 0, getWidth(), getHeight());//DESENHA O QUADRADO DO FUNDO
        //g.setColor(Color.red);//define cor vermelha pro chão
        //g.drawLine(0, (int)GROUNDy, getWidth(), (int)GROUNDy); //DESENHA O CHÃO

        g.setColor(Color.BLACK);
        g.drawString("HI " + character.score, 500, 20);

        switch (gameState) {
            case START_GAME_STATE:
                character.draw(g);
                break;
            case GAME_PLAYING_STATE:
                clouds.draw(g);
                land.draw(g);
                character.draw(g);
                enemiesManager.draw(g);
                g.setColor(Color.BLACK);
                g.drawString("HI " + score, 300, 20);
                break;
            case GAME_OVER_STATE:
                clouds.draw(g);
                land.draw(g);
                character.draw(g);
                enemiesManager.draw(g);
                g.drawImage(gameOverButtonImage, 100, 50, null);
                break;
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {
        //TODO Auto-generated method stub
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!isKeyPressed) {
            isKeyPressed = true;
            switch (gameState) {
                case START_GAME_STATE:
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        gameState = GAME_PLAYING_STATE;
                    }
                    break;
                case GAME_PLAYING_STATE:
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        character.jump();
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        character.down(true);
                    }
                    break;
                case GAME_OVER_STATE:
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        gameState = GAME_PLAYING_STATE;
                        resetGame();
                    }
                    break;

            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        isKeyPressed = false;
        if (gameState == GAME_PLAYING_STATE) {
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                character.down(false);
            }
        }
    }


    @Override
    public void run() {

        // Displaying the thread that is running
        System.out.println(
                "Thread " + Thread.currentThread().getId()
                        + " is running");

        while (true) {

            try {
                for (int i = 0; i < 2; i++) {

                    MainCharacter mainCharacter = new MainCharacter();
                }


                update();
                repaint();
                //Thread.sleep(msSleep, nanoSleep);
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void update() {
        switch (gameState) {
            case GAME_PLAYING_STATE:
                character.update();
                land.update();
                clouds.update();
                enemiesManager.update();

                if (!character.isAlive()) {
                    gameState = GAME_OVER_STATE;
                }
                break;
        }
    }

    private void resetGame() {
        character.setX(50);
        character.setY(50);
        character.setAlive(true);
        enemiesManager.reset();

//        character.dead(false);
//        character.reset();
    }

}
