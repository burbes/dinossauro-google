import enums.GameStateEnum;
import enums.MainCharacterStateEnum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class GameScreen extends JPanel implements Runnable, KeyListener {

    private float espaco = 0;
    public static long startTime = 0;
    public static long currentTime = 0;

    public static float VELOCIDADE = 3f;
    public static final float GRAVITY = 0.5f;
    public static final float GROUNDy = 110; // by pixel
    public static final int QUANTIDADE_DINOS = 1000;

    private int GERACAO = 0;

    private List<MainCharacter> dinossaurs;
    private boolean isKeyPressed;

    private Thread thread;

    private Clouds clouds;
    private Land land;
    private EnemiesManager enemiesManager;

    private GameStateEnum gameState = GameStateEnum.START_GAME_STATE;
    private BufferedImage gameOverButtonImage;

    private EstadoDinossauro estadoDinossauro;

    private int melhorScore;
    private int scoreAtual;

    public GameScreen() {
        thread = new Thread(this);
        gameOverButtonImage = Resource.getResourceImage("data/gameover_text.png");

        inicializarDinossauros();
        estadoDinossauro = new EstadoDinossauro();
        land = new Land();
        clouds = new Clouds();
        enemiesManager = new EnemiesManager(dinossaurs);
        GERACAO++;
        melhorScore = 0;
        scoreAtual = 0;
    }

    private List<MainCharacter> inicializarDinossauros() {

        dinossaurs = new ArrayList<>();

        for (int i = 0; i < QUANTIDADE_DINOS; i++) {
            MainCharacter character = new MainCharacter();

//            for (int j = 0; j < character.tamanhoDNA; j++) {
//                if (DNA.DNADaVez[j] != null) {
//                    character.DNA[j] = DNA.DNADaVez[i][j]; //TODO: NUNCA ESTÁ CAINDO AQUI
//                } else {
//                    character.DNA[j] = Uteis.getRandomValue();
//                }
//            }
            dinossaurs.add(character);
            //NeuralNetwork.RNA_CopiarVetorParaCamadas(dinossaurs.get(i).cerebro, dinossaurs.get(i).DNA);
        }
        DNA.inicializarDNA(dinossaurs); //JÁ INICIALIZOU
        return dinossaurs;
    }

    public void startGame() {
        startTime = System.currentTimeMillis();
        thread.start();
    }

//    public void drawGraph(Graphics g){
//
//        int width = getWidth();
//        int height = getHeight();
//
//        // Draw X and Y axes
//        g.setColor(Color.BLACK);
//        g.drawLine(700, GameWindow.SCREEN_HEIGHT-100, GameWindow.SCREEN_WIDTH-100, GameWindow.SCREEN_HEIGHT-100);
//        g.drawLine(750, GameWindow.SCREEN_HEIGHT, 750, GameWindow.SCREEN_HEIGHT/2);
//
//        // Draw grid
//        g.setColor(Color.LIGHT_GRAY);
//        for (int i = 0; i < width; i += GameWindow.SCREEN_WIDTH) {
//            g.drawLine(i, 0, i, height);
//        }
//        for (int i = 0; i < height; i += GameWindow.SCREEN_HEIGHT) {
//            g.drawLine(0, i, width, i);
//        }
//
//        // Draw your graph or shapes here
//        // For example, draw a red line from (10, 10) to (100, 100)
//        g.setColor(Color.RED);
//        g.drawLine(10, 10, 100, 100);
//
//        g.setColor(Color.BLUE);
//        for (int i = 0; i < xPoints.length; i++) {
//            g.fillOval(xPoints[i] - 3, getHeight() - yPoints[i] - 3, 6, 6);
//        }
//    }

    private int[] xPoints = {50, 150, 250, 350, 450};
    private int[] yPoints = {100, 200, 150, 300, 250};

    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(Color.decode("#f7f7f7"));
        g.fillRect(0, 0, getWidth(), getHeight());//DESENHA O QUADRADO DO FUNDO

        g.setColor(Color.BLACK);

        g.drawString("HI: " + scoreAtual, 500, 20);

        g.drawString("Time Diff: " + (currentTime - startTime) / 1000 + "s", 450, GameWindow.SCREEN_HEIGHT - 280);
        g.drawString("Current Time: " + Uteis.getDateFormated(currentTime), 250, GameWindow.SCREEN_HEIGHT - 280);
        g.drawString("Start Time: " + Uteis.getDateFormated(startTime), 40, GameWindow.SCREEN_HEIGHT - 280);

        g.drawString("Melhor Score: " + melhorScore, 40, GameWindow.SCREEN_HEIGHT - 260);
        g.drawString("Dinos total: " + dinossaurs.size(), 40, GameWindow.SCREEN_HEIGHT - 240);
        g.drawString("Dinos vivos: " + (dinossaurs.stream().filter(MainCharacter::isAlive).count()), 40, GameWindow.SCREEN_HEIGHT - 220);

        g.drawString("Distancia do Prox Obstáculo: " + enemiesManager.getDistanciaAtePersonagem(), 40, GameWindow.SCREEN_HEIGHT - 200);
        g.drawString("Altura do Prox Obstáculo: " + (100 - enemiesManager.getEnemy().getBound().getY()), 40, GameWindow.SCREEN_HEIGHT - 160);
        g.drawString("Largura do Prox Obstáculo: " + enemiesManager.getEnemy().getBound().getWidth(), 40, GameWindow.SCREEN_HEIGHT - 180);
        g.drawString("Comprimento do Prox Obstáculo: " + enemiesManager.getEnemy().getBound().getHeight(), 40, GameWindow.SCREEN_HEIGHT - 140);

        g.drawString("Velocidade: " + VELOCIDADE, 40, GameWindow.SCREEN_HEIGHT - 120);
        g.drawString("Geração: " + GERACAO, 40, GameWindow.SCREEN_HEIGHT - 100);
        g.drawString("Naelson Matheus Junior naelsonmjunior@gmail.com ", GameWindow.SCREEN_WIDTH - 400, GameWindow.SCREEN_HEIGHT - 100);

        switch (gameState) {

            case START_GAME_STATE:
                dinossaurs.stream().findAny().get().draw(g);
                break;
            case GAME_PLAYING_STATE:
                clouds.draw(g);
                land.draw(g);

                for (int i = 0; i < dinossaurs.size(); i++) {
                    MainCharacter dino = dinossaurs.get(i);
                    if (dino.isAlive())
                        dino.draw(g);
                }
                enemiesManager.draw(g);
                g.setColor(Color.BLACK);

                break;
            case GAME_OVER_STATE:
//                    clouds.draw(g);
//                    land.draw(g);
//                    dino.draw(g);
//                    enemiesManager.draw(g);
                g.drawImage(gameOverButtonImage, 100, 50, null);
                try {
                    Thread.sleep(1000); //espera 1 segundo e reinicia o jogo
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                gameState = GameStateEnum.GAME_PLAYING_STATE;
                resetGame();
                break;
        }
//        drawGraph(g);
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
                VELOCIDADE = espaco++ / (currentTime - startTime);
                for (int i = 0; i < dinossaurs.size(); i++) {
                    MainCharacter dino = dinossaurs.get(i);
                    MainCharacterStateEnum estadoDinoEnum = estadoDinossauro.controlarEstadoDinossauros(dino, enemiesManager.getEnemy());

                    if (estadoDinoEnum.equals(MainCharacterStateEnum.JUMPING)) {
                        dino.jump();
                    } else if (estadoDinoEnum.equals(MainCharacterStateEnum.DOWN_RUN)) {
                        dino.down(true);
                    }

                }
                update();

                currentTime = System.currentTimeMillis();

                repaint();
                //Thread.sleep(msSleep, nanoSleep);
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void update() {
        switch (gameState) {
            case GAME_PLAYING_STATE:

                for (MainCharacter dino : dinossaurs) {
                    dino.update();
                }
                scoreAtual = dinossaurs.stream().map(MainCharacter::getScore).max(Comparator.comparing(d -> d)).orElse(0);
                enemiesManager.update();
                land.update();
                clouds.update();

                if (dinossaurs.stream().allMatch(dino -> !dino.isAlive())) {
                    gameState = GameStateEnum.GAME_OVER_STATE;
                }
                break;
        }
    }

    private void resetGame() {
        melhorScore();

        //clonar o melhor
        List<MainCharacter> os2melhorDino = dinossaurs.stream().sorted(Comparator.comparing(MainCharacter::getScore).reversed()).limit(2).collect(Collectors.toList());
        List<MainCharacter> newDinos = inicializarDinossauros();
        List<MainCharacter> dinosClonados = DNA.randomMutations(newDinos, os2melhorDino.toArray(MainCharacter[]::new));
        dinossaurs = new ArrayList<>();
//        dinossaurs.add(melhorDino);
        dinossaurs.addAll(dinosClonados);
        GERACAO++;

        enemiesManager.reset();
        enemiesManager = new EnemiesManager(dinossaurs);
    }

    private void melhorScore() {
        int aux = dinossaurs.stream().map(MainCharacter::getScore).max(Comparator.comparing(d -> d)).orElse(0);
        melhorScore = aux > melhorScore ? aux : melhorScore;
    }

}
