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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GameScreen extends JPanel implements Runnable, KeyListener {


    public static final float VELOCIDADE = 3f;
    public static final float GRAVITY = 0.5f;
    public static final float GROUNDy = 110; // by pixel
    public static final int QUANTIDADE_DINOS = 10;

    private int GERACAO = 0;

    ;
//    private MainCharacter character1;
//    private MainCharacter character2;

    private List<MainCharacter> dinossaurs;
    private boolean isKeyPressed;

    private Thread thread;


    private int threadsCount = 2;

    private Clouds clouds;
    private Land land;
    private EnemiesManager enemiesManager;

    private GameStateEnum gameState = GameStateEnum.START_GAME_STATE;
    private BufferedImage gameOverButtonImage;

    private EstadoDinossauro estadoDinossauro;

    public GameScreen() {
        thread = new Thread(this);
        gameOverButtonImage = Resource.getResourceImage("data/gameover_text.png");

        dinossaurs = new ArrayList<>();

        for (int i = 0; i < QUANTIDADE_DINOS; i++) {
            MainCharacter character = new MainCharacter();
            character.setX(50); //empurra o dino um pouco pra frente
            character.setY(60);
            character.cerebro = NeuralNetwork.RNA_CriarRedeNeural(6, 6, 6, 2);
            dinossaurs.add(character);
        }

        estadoDinossauro = new EstadoDinossauro();

        land = new Land(GameWindow.SCREEN_WIDTH);
        clouds = new Clouds();
        enemiesManager = new EnemiesManager();
        GERACAO++;
    }

    public void startGame() {
        thread.start();
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.decode("#f7f7f7"));
        g.drawRect(0, 0, getWidth(), getHeight());//DESENHA O QUADRADO DO FUNDO
        //g.setColor(Color.red);//define cor vermelha pro chão
        //g.drawLine(0, (int)GROUNDy, getWidth(), (int)GROUNDy); //DESENHA O CHÃO

        g.setColor(Color.BLACK);

        //g.drawString("HI " + character.score, 500, 20);

        dinossaurs.forEach(dino -> {

            g.drawString("Dinos total: " + dinossaurs.size(), 40, GameWindow.SCREEN_HEIGHT - 240);
            g.drawString("Dinos vivos: " + (dinossaurs.stream().filter(MainCharacter::isAlive).count()), 40, GameWindow.SCREEN_HEIGHT - 220);
            g.drawString("Distancia   do Prox Obstáculo: " + enemiesManager.getDistanciaAtePersonagem(), 40, GameWindow.SCREEN_HEIGHT - 200);
            g.drawString("Altura      do Prox Obstáculo: " + enemiesManager.getEnemy().getBound().getY(), 40, GameWindow.SCREEN_HEIGHT - 160);
            g.drawString("Largura     do Prox Obstáculo: " + enemiesManager.getEnemy().getBound().getWidth(), 40, GameWindow.SCREEN_HEIGHT - 180);
            g.drawString("Comprimento do Prox Obstáculo: " + enemiesManager.getEnemy().getBound().getHeight(), 40, GameWindow.SCREEN_HEIGHT - 140);
            g.drawString("Velocidade: " + VELOCIDADE, 40, GameWindow.SCREEN_HEIGHT - 120);
            g.drawString("Geração: " + GERACAO, 40, GameWindow.SCREEN_HEIGHT - 100);

            switch (gameState) {

                case START_GAME_STATE:
                    dino.draw(g);
                    break;
                case GAME_PLAYING_STATE:
                    clouds.draw(g);
                    land.draw(g);
                    if (dino.isAlive())
                        dino.draw(g);
                    enemiesManager.draw(g);
                    g.setColor(Color.BLACK);

                    break;
                case GAME_OVER_STATE:
                    clouds.draw(g);
                    land.draw(g);
                    dino.draw(g);
                    enemiesManager.draw(g);
                    g.drawImage(gameOverButtonImage, 100, 50, null);
                    try {
                        Thread.sleep(1000); //espera 2 segundo e reinicia o jogo
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    gameState = GameStateEnum.GAME_PLAYING_STATE;
                    resetGame();
                    break;
            }
        });
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
                        gameState = GameStateEnum.GAME_PLAYING_STATE;
                    }
                    break;
                case GAME_PLAYING_STATE:
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        dinossaurs.get(2).jump();//TODO
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        //dinossaurs.get(1).down(true);//TODO
                    }
                    break;
                case GAME_OVER_STATE:
                    boolean todosMortos = !dinossaurs.stream().allMatch(dino -> dino.isAlive());
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        gameState = GameStateEnum.GAME_PLAYING_STATE;
                        resetGame();
                    }
                    break;

            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        isKeyPressed = false;
        if (gameState == GameStateEnum.GAME_PLAYING_STATE) {
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                //character.down(false);
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
                dinossaurs.forEach(dino -> {
                    EstadoDinoEnum estadoDinoEnum = estadoDinossauro.controlarEstadoDinossauros(dino, enemiesManager.getEnemy());

                    if (estadoDinoEnum.equals(EstadoDinoEnum.PULAR)) {
                        dino.jump();
                    } else if (estadoDinoEnum.equals(EstadoDinoEnum.ABAIXAR)) {
                        dino.down(true);
                    }
                    update(dino);

                });

                repaint();
                //Thread.sleep(msSleep, nanoSleep);
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void update(MainCharacter character) {
        switch (gameState) {
            case GAME_PLAYING_STATE:
                character.update();
                land.update();
                clouds.update();
                enemiesManager.update(character);

                if (dinossaurs.stream().allMatch(dino -> !dino.isAlive())) {
                    gameState = GameStateEnum.GAME_OVER_STATE;
                }
                break;
        }
    }

    private void resetGame() {
        GERACAO++;
        for (MainCharacter dino : dinossaurs) {
            dino.setX(50);
            dino.setY(50);
            dino.setAlive(true);
        }
        //clonar os 2 melhores


        //enemiesManager.reset();
        enemiesManager = new EnemiesManager();
    }

}
