
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Clouds {

    private int NUMBER_OF_CLOUDS = 20;

    private BufferedImage cloudImage;
    private List<Cloud> clouds;


    public Clouds() {
        cloudImage = Resource.getResourceImage("data/cloud.png");

        clouds = new ArrayList<Cloud>();

        generateClouds();

    }

    private void generateClouds() {
        int initialPosX = GameWindow.SCREEN_WIDTH;
        int posYOffset = 30;
        int distanceBetweenClouds = 150;
        for (int i = 0; i < NUMBER_OF_CLOUDS; i++) {
            Cloud imageCloud = new Cloud();
            imageCloud.posX = initialPosX - (i * distanceBetweenClouds);
            imageCloud.posY = posYOffset + (i * 10) % 40; // Este cálculo determina a posição Y de cada nuvem
            clouds.add(imageCloud);
        }
    }

    public void update(){
        for (Cloud cloud : clouds){
            cloud.posX -= 2;//velocidade que as nuvens se movem
        }
        Cloud firstCloud = clouds.stream().min(Comparator.comparing(c -> c.posX)).get();
        if(firstCloud.posX + cloudImage.getWidth() < 0){
            firstCloud.posX = GameWindow.SCREEN_WIDTH;
            clouds.remove(firstCloud);
            clouds.add(firstCloud);
        }
    }


    public void draw(Graphics g) {
        for (Cloud cloud : clouds){
            g.drawImage(cloudImage, (int) cloud.posX, (int) cloud.posY, null);
        }
    }

}
