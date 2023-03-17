
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Clouds {



    private BufferedImage cloudImage;
    private List<Cloud> clouds;


    public Clouds() {
        cloudImage = Resource.getResourceImage("data/cloud.png");

        clouds = new ArrayList<Cloud>();


        Cloud imageCloud = new Cloud();
        imageCloud.posX = 0;
        imageCloud.posY = 30;
        clouds.add(imageCloud);

        imageCloud = new Cloud();
        imageCloud.posX = 150;
        imageCloud.posY = 40;
        clouds.add(imageCloud);

        imageCloud = new Cloud();
        imageCloud.posX = 300;
        imageCloud.posY = 50;
        clouds.add(imageCloud);

        imageCloud = new Cloud();
        imageCloud.posX = 450;
        imageCloud.posY = 20;
        clouds.add(imageCloud);

        imageCloud = new Cloud();
        imageCloud.posX = 600;
        imageCloud.posY = 60;
        clouds.add(imageCloud);

    }

    public void update(){
        for (Cloud cloud : clouds){
            cloud.posX -= 2;//velocidade que as nuvens se movem
        }
        Cloud firstCloud = clouds.get(0);
        if(firstCloud.posX + cloudImage.getWidth() < 0){
            firstCloud.posX = 600;
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
