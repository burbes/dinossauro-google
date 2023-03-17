import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Land {

    private BufferedImage imageLand1;
    private BufferedImage imageLand2;
    private BufferedImage imageLand3;

    private List<ImageLand> listLand;
    public Land(int width) {

        imageLand1 = Resource.getResourceImage("data/land1.png");
        imageLand2 = Resource.getResourceImage("data/land2.png");
        imageLand3 = Resource.getResourceImage("data/land3.png");

        int numberOfImageLand = width / imageLand1.getWidth() + 2;
        listLand = new ArrayList<ImageLand>();
        for(int i = 0; i < numberOfImageLand; i++) {
            ImageLand imageLand = new ImageLand();
            imageLand.posX = i * imageLand1.getWidth();
            imageLand.image = getImage();
            listLand.add(imageLand);
        }
    }


    public void update(){
        for (ImageLand imageLand : listLand){
            imageLand.posX--;
        }
        ImageLand firstElement = listLand.get(0);
        if(firstElement.posX + imageLand1.getWidth() < 0){
            firstElement.posX = listLand.get(listLand.size() - 1).posX + imageLand1.getWidth();
            listLand.add(firstElement);
            listLand.remove(0);
        }
    }

    public void draw(Graphics g) {
        for(ImageLand imgLand : listLand) {
            g.drawImage(imgLand.image, (int) imgLand.posX, (int)GameScreen.GROUNDy - 20, null);
        }
    }


    private BufferedImage getImage() {
        Random rand = new Random();

        int i = rand.nextInt(3);
        switch (i){
            case 0 : return imageLand1;
            case 1 : return imageLand3;
            default : return imageLand2;
        }
    }

}
