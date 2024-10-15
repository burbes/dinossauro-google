import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Clouds {

    private BufferedImage cloudImage;
    private List<Cloud> clouds;

    public Clouds() {
        cloudImage = Resource.getImage("data/cloud.png");
        clouds = new ArrayList<>();
        initializeClouds();
    }

    private void initializeClouds() {
        int initialPosX = 0;
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            Cloud cloud = new Cloud();
            cloud.posX = initialPosX + i * 150;
            cloud.posY = random.nextInt(100);
            clouds.add(cloud);
        }
    }

    public void update() {
        for (Cloud cloud : clouds) {
            cloud.posX -= GameScreen.globalSpeed / 2; // As nuvens podem se mover mais lentamente
        }
        Cloud firstCloud = clouds.get(0);
        if (firstCloud.posX + cloudImage.getWidth() < 0) {
            firstCloud.posX = GameScreen.SCREEN_WIDTH;
            clouds.add(clouds.remove(0));
        }
    }

    public void draw(Graphics g) {
        for (Cloud cloud : clouds) {
            g.drawImage(cloudImage, (int) cloud.posX, (int) cloud.posY, null);
        }
    }
}
