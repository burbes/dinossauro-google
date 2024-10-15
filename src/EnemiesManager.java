import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class EnemiesManager {

    private BufferedImage cactusImage1;
    private BufferedImage cactusImage2;

    private List<Enemy> enemies;
    private Random rand;
    private List<MainCharacter> dinosaurs;

    private int enemiesPassed = 0;

    public EnemiesManager(List<MainCharacter> dinosaurs) {
        this.dinosaurs = dinosaurs;
        rand = new Random();
        enemies = new ArrayList<>();

        cactusImage1 = Resource.getImage("data/cactus1.png");
        cactusImage2 = Resource.getImage("data/cactus2.png");

        enemies.add(createRandomEnemy());
    }

    public Enemy getCurrentEnemy() {
        return enemies.stream().findFirst().orElseGet(() -> {
            Enemy newEnemy = createRandomEnemy();
            enemies.add(newEnemy);
            return newEnemy;
        });
    }

    public void update() {
        Iterator<Enemy> iterator = enemies.iterator();
        List<Enemy> newEnemies = new ArrayList<>(); // Lista para novos inimigos
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            enemy.update();

            for (MainCharacter dinosaur : dinosaurs) {
                if (dinosaur.isAlive() && enemy.isCollidingWith(dinosaur)) {
                    dinosaur.die();
                }
                if (enemy.isPassedBy(dinosaur) && !enemy.isScoreCounted()) {
                    dinosaur.increaseScore();
                    enemy.setScoreCounted(true);
                    enemiesPassed++; // Incrementa quando um inimigo é ultrapassado
                }
            }

            if (enemy.isOffScreen()) {
                iterator.remove(); // Remoção segura
                newEnemies.add(createRandomEnemy()); // Coleta o novo inimigo
            }
        }
        enemies.addAll(newEnemies); // Adiciona novos inimigos após a iteração
    }

    public void draw(Graphics g) {
        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }
    }

    public void reset() {
        enemies.clear();
        enemies.add(createRandomEnemy());
        enemiesPassed = 0; // Reseta o contador
    }

    private Enemy createRandomEnemy() {
        if (rand.nextBoolean()) {
            return new Cactus(cactusImage1, cactusImage2);
        } else {
            return new Pterodactyl();
        }
    }


    public int getEnemiesPassed() {
        return enemiesPassed;
    }
    public void setDinosaurs(List<MainCharacter> dinosaurs) {
        this.dinosaurs = dinosaurs;
    }


}
