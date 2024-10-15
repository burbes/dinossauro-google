import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class GameScreen extends JPanel implements Runnable, KeyListener {

    // Constantes
    public static final float GRAVITY = 0.8f; // Valor anterior era 0.5f
    public static final float GROUND_Y = 110; // Em pixels
    public static final int TOTAL_DINOSAURS = 1000;
    public static final int SCREEN_WIDTH = 1200;
    public static final int SCREEN_HEIGHT = 500;
    public static final int MAX_CLOCK = 10; // Valor máximo para o clock

    // Variáveis do jogo
    private int bestFitness = 0;
    private double averageFitness = 0.0;

    public static double mutationRate = 0.05; // Taxa de mutação inicial de 5%
    public static float globalSpeed = 5f; // Valor inicial da velocidade global
    public static float distanceTraveled = 0;
    public static long startTime = 0;
    public static long currentTime = 0;
    public static long clock = 1;
    public static float speed = 3f;
    public static float baseSpeed = 5f; // Valor inicial da velocidade base
    public static int generation = 0;
    public static int bestScore = 0;
    public static int currentScore = 0;

    // Objetos do jogo
    private List<MainCharacter> dinosaurs;
    private List<MainCharacter> bestDinosaurs;
    private Thread thread;
    private Clouds clouds;
    private Land land;
    private EnemiesManager enemiesManager;
    private GameStateEnum gameState = GameStateEnum.PLAYING;
    private DinosaurStateController dinosaurStateController;

    public GameScreen() {
        thread = new Thread(this);
        dinosaurs = new ArrayList<>();
        bestDinosaurs = new ArrayList<>();
        for (int i = 0; i < TOTAL_DINOSAURS; i++) {
            dinosaurs.add(new MainCharacter());
        }
        // Inicializa bestDinosaurs com os dois primeiros dinossauros
        bestDinosaurs.add(dinosaurs.get(0));
        bestDinosaurs.add(dinosaurs.get(1));
        dinosaurStateController = new DinosaurStateController();
        land = new Land();
        clouds = new Clouds();
        enemiesManager = new EnemiesManager(dinosaurs);
        generation++;
    }

    public void startGame() {
        startTime = System.currentTimeMillis();
        thread.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Desenha o fundo
        g.setColor(Color.decode("#f7f7f7"));
        g.fillRect(0, 0, getWidth(), getHeight());

        // Desenha informações do jogo
        g.setColor(Color.BLACK);
        g.drawString("HI: " + currentScore, 500, 20);
        g.drawString("Time: " + (currentTime - startTime) / 1000 + "s", 450, SCREEN_HEIGHT - 280);
        g.drawString("Best Score: " + bestScore, 40, SCREEN_HEIGHT - 260);
        g.drawString("Generation: " + generation, 40, SCREEN_HEIGHT - 80);
        g.drawString("Speed: " + String.format("%.2f", globalSpeed), 40, SCREEN_HEIGHT - 120);
        g.drawString("Clock: " + clock, 40, SCREEN_HEIGHT - 100);

        int aliveDinosaurs = (int) dinosaurs.stream().filter(MainCharacter::isAlive).count();
        g.drawString("Dinosaurs Alive: " + aliveDinosaurs + "/" + TOTAL_DINOSAURS, 40, SCREEN_HEIGHT - 140);

        int bestFitness = dinosaurs.stream().mapToInt(MainCharacter::getFitness).max().orElse(0);
        g.drawString("Best Fitness: " + bestFitness, 40, SCREEN_HEIGHT - 160);

        double averageFitness = dinosaurs.stream().mapToInt(MainCharacter::getFitness).average().orElse(0);
        g.drawString("Average Fitness: " + String.format("%.2f", averageFitness), 40, SCREEN_HEIGHT - 180);
        g.drawString("Mutation Rate: " + String.format("%.2f", mutationRate * 100) + "%", 40, SCREEN_HEIGHT - 200);
        g.drawString("Distance: " + String.format("%.2f", distanceTraveled), 500, 40);
        g.drawString("Enemies Passed: " + enemiesManager.getEnemiesPassed(), 500, 60);

        // Desenha elementos do jogo
        clouds.draw(g);
        land.draw(g);
        for (MainCharacter dino : dinosaurs) {
            if (dino.isAlive()) {
                dino.draw(g);
            }
        }
        enemiesManager.draw(g);

        // Se quiser, pode exibir uma mensagem quando o jogo está em GAME_OVER
        if (gameState == GameStateEnum.GAME_OVER) {
            g.setColor(Color.RED);
            g.drawString("Game Over", SCREEN_WIDTH / 2 - 50, SCREEN_HEIGHT / 2);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (gameState == GameStateEnum.PLAYING) {
                    // Aumenta a velocidade base a cada 100 pontos
                    if (currentScore % 100 == 0 && currentScore != 0) {
                        baseSpeed += 0.1f;
                    }
                    // Ajuste a velocidade global com base no clock
                    globalSpeed = baseSpeed * clock;

                    // Atualize a velocidade de acordo com a distância percorrida ou pontuação, se desejar
                    // Por exemplo, aumentar a velocidade gradualmente ao longo do tempo

                    for (MainCharacter dino : dinosaurs) {
                        MainCharacterState state = dinosaurStateController.controlDinosaurState(dino, enemiesManager.getCurrentEnemy());
                        if (state == MainCharacterState.JUMPING) {
                            dino.jump();
                        } else if (state == MainCharacterState.DUCKING) {
                            dino.duck(true);
                        } else {
                            dino.duck(false);
                        }
                    }
                    updateGame();
                    currentTime = System.currentTimeMillis();
                    repaint();
                }
                Thread.sleep(Math.max(1, 25 / clock));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateGame() {
        if (gameState == GameStateEnum.PLAYING) {
            for (MainCharacter dino : dinosaurs) {
                dino.update();
            }
            currentScore = dinosaurs.stream().mapToInt(MainCharacter::getScore).max().orElse(0);
            enemiesManager.update();
            land.update();
            clouds.update();
            distanceTraveled += globalSpeed; // Atualiza a distância percorrida

            // Atualiza estatísticas de fitness
            updateFitnessStats();

            if (dinosaurs.stream().noneMatch(MainCharacter::isAlive)) {
                resetGame();
            }
        }
    }

    private void resetGame() {
        updateBestScore();
        enemiesManager.reset();
        dinosaurs = sortDinosaursByFitness();
        List<MainCharacter> topDinosaurs = dinosaurs.subList(0, Math.min(2, dinosaurs.size()));
        updateBestDinosaursEver(topDinosaurs);
        if (bestDinosaurs.size() < 2) {
            // Se não houver dois dinossauros, preencha com cópias dos melhores disponíveis
            while (bestDinosaurs.size() < 2) {
                bestDinosaurs.add(new MainCharacter());
            }
        }
        // Remova a chamada para salvar a rede neural se não estiver implementada
        // NeuralNetwork.saveNetwork(bestDinosaurs.get(0).getBrain(), generation, Utility.getFormattedDate(startTime));
        generation++;

        // Recriar a lista de dinossauros com novos cérebros
        List<MainCharacter> newDinosaurs = new ArrayList<>();
        for (int i = 0; i < TOTAL_DINOSAURS; i++) {
            MainCharacter dino = new MainCharacter();
            dino.setBrain(NeuralNetwork.crossoverAndMutate(
                    bestDinosaurs.get(0).getBrain(),
                    bestDinosaurs.get(1).getBrain(),
                    mutationRate // Use a variável mutationRate que você declarou
            ));
            dino.reset(); // Reset o estado do dinossauro
            newDinosaurs.add(dino);
        }
        dinosaurs = newDinosaurs;
        enemiesManager.setDinosaurs(dinosaurs); // Atualiza a referência dos dinossauros no EnemiesManager

        // Resetar outras variáveis do jogo
        baseSpeed = 5f;
        globalSpeed = baseSpeed * clock;
        distanceTraveled = 0;
        startTime = System.currentTimeMillis();
        currentTime = startTime;
        gameState = GameStateEnum.PLAYING;


    }

    private List<MainCharacter> sortDinosaursByFitness() {
        return dinosaurs.stream()
                .sorted(Comparator.comparing(MainCharacter::getFitness).reversed())
                .collect(Collectors.toList());
    }

    private void updateBestDinosaursEver(List<MainCharacter> topDinosaurs) {
        bestDinosaurs.clear();
        bestDinosaurs.addAll(topDinosaurs);
    }

    private void updateBestScore() {
        int maxScore = dinosaurs.stream().mapToInt(MainCharacter::getScore).max().orElse(0);
        bestScore = Math.max(maxScore, bestScore);

        int maxFitness = dinosaurs.stream().mapToInt(MainCharacter::getFitness).max().orElse(0);
        bestFitness = Math.max(maxFitness, bestFitness);
    }

    // Implementação dos métodos KeyListener
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_UP) {
            if (clock < MAX_CLOCK) {
                clock++;
            }
        } else if (keyCode == KeyEvent.VK_DOWN) {
            if (clock > 1) {
                clock--;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Não utilizado neste contexto
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Não utilizado
    }

    private void updateFitnessStats() {
        int maxFitness = dinosaurs.stream().mapToInt(MainCharacter::getFitness).max().orElse(0);
        bestFitness = Math.max(maxFitness, bestFitness);

        averageFitness = dinosaurs.stream().mapToInt(MainCharacter::getFitness).average().orElse(0.0);
    }

}
