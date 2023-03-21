import com.sun.tools.javac.Main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemiesManager {

    private BufferedImage cactus1;
    private BufferedImage cactus2;

    private List<Enemy> enemies;
    private Random rand;

    private double distanciaAtePersonagem;

    public EnemiesManager() {
        rand = new Random();
        enemies = new ArrayList<Enemy>();

        cactus1 = Resource.getResourceImage("data/cactus1.png");
        cactus2 = Resource.getResourceImage("data/cactus2.png");

        enemies.add(getRandomCactus());
        distanciaAtePersonagem = 0.0f;
    }

    public Enemy getEnemy(){
        return enemies.stream().findFirst().orElseGet(() -> {
            enemies.add(getRandomCactus());
            return enemies.stream().findFirst().get();
        });
    }

    public void update(MainCharacter mainCharacter) {
        for (Enemy e : enemies) {
            e.update();

            if(e.isOver(mainCharacter) && !e.isScoreGot()){
                mainCharacter.upScore();
                e.setScoreGot(true);
            }

            if (mainCharacter.getBound().intersects(e.getBound())){
                mainCharacter.setAlive(false);
                mainCharacter.dead(true);

            }
            distanciaAtePersonagem = mainCharacter.getX() - e.getBound().getX();
        }


        Enemy enemy = enemies.get(0);
        if (enemy.isOutOfScreen()) {
            //mainCharacter.upScore();
            enemies.clear();
            enemies.add(getRandomCactus());
        }

    }

    public void draw(Graphics g) {
        for(Enemy e : enemies) {
            e.draw(g);
        }
    }

    private Cactus getRandomCactus(){
        Cactus cactus;
        cactus = new Cactus();
        cactus.setX(600);
        if(rand.nextBoolean()){
            cactus.setImage(cactus1);
        } else {
            cactus.setImage(cactus2);
        }
        return cactus;
    }

    public void reset() {
        enemies.clear();
        enemies.add(getRandomCactus());
    }

    public double getDistanciaAtePersonagem() {
        return distanciaAtePersonagem;
    }
}
