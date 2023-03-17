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

    private MainCharacter mainCharacter;
    private GameScreen gameScreen;


    public EnemiesManager(MainCharacter mainCharacter, GameScreen gameScreen) {
        this.mainCharacter = mainCharacter;
        this.gameScreen = gameScreen;
        rand = new Random();
        enemies = new ArrayList<Enemy>();

        cactus1 = Resource.getResourceImage("data/cactus1.png");
        cactus2 = Resource.getResourceImage("data/cactus2.png");

        enemies.add(getRandomCactus());

    }

    public void update() {
        for (Enemy e : enemies) {
            e.update();

            if(e.isOver() && !e.isScoreGot()){
                gameScreen.plusScore(20);
                e.setScoreGot(true);
            }

            if (mainCharacter.getBound().intersects(e.getBound())){
                mainCharacter.setAlive(false);

            }
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
        cactus = new Cactus(mainCharacter);
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

}
