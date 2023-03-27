import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemiesManager {

    private BufferedImage cactus1;
    private BufferedImage cactus2;
    private BufferedImage pterodactyl;

    private List<Enemy> enemies;
    private Random rand;

    private double distanciaAtePersonagem;

    private List<MainCharacter> dinossaurs;

    public EnemiesManager(List<MainCharacter> dinossaurs) {
        this.dinossaurs = dinossaurs;
        rand = new Random();
        enemies = new ArrayList<Enemy>();

        cactus1 = Resource.getResourceImage("data/cactus1.png");
        cactus2 = Resource.getResourceImage("data/cactus2.png");
        pterodactyl = Resource.getResourceImage("data/pterodacty.jpg");

        enemies.add(getRandomEnemy());

        distanciaAtePersonagem = 0.0f;
    }

    public Enemy getEnemy() {
        return enemies.stream().findFirst().orElseGet(() -> {
            enemies.add(getRandomCactus());
            return enemies.stream().findFirst().get();
        });
    }

    public void update() {
        for (Enemy e : enemies) {
            e.update();
            for (MainCharacter mainCharacter : dinossaurs) {
                if (e.isOver(mainCharacter) && !e.isScoreGot()) {
                    mainCharacter.upScore();
                    e.setScoreGot(true);
                }

                if (mainCharacter.getBound().intersects(e.getBound())) {
                    mainCharacter.setAlive(false);
                    mainCharacter.dead(true);

                }
                distanciaAtePersonagem = mainCharacter.getX() - e.getBound().getX();
            }
            if (e.isOutOfScreen()) {
                enemies.remove(e);
                enemies.add(getRandomEnemy());
            }
        }
    }

    public void draw(Graphics g) {
        for (Enemy e : enemies) {
            e.draw(g);
        }
    }

    private Enemy getRandomEnemy() {
        if (rand.nextBoolean()) {
            return getRandomCactus();
        } else {
            return getPterodactyl();
        }
    }

    private Cactus getRandomCactus() {
        Cactus cactus;
        cactus = new Cactus();
        if (rand.nextBoolean()) {
            cactus.setImage(cactus1);
        } else {
            cactus.setImage(cactus2);
        }
        return cactus;
    }

    private Pterodactyl getPterodactyl() {
        Pterodactyl pterodactyl = new Pterodactyl();
        return pterodactyl;
    }

    public void reset() {
        enemies = new ArrayList<>();
        enemies.add(getRandomEnemy());
    }

    public double getDistanciaAtePersonagem() {
        return distanciaAtePersonagem;
    }
}
