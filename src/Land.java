import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Land {

    private BufferedImage landImage1;
    private BufferedImage landImage2;
    private BufferedImage landImage3;
    private List<ImageLand> landImages;

    public Land() {
        landImage1 = Resource.getImage("data/land1.png");
        landImage2 = Resource.getImage("data/land2.png");
        landImage3 = Resource.getImage("data/land3.png");

        landImages = new ArrayList<>();
        int numberOfImages = GameScreen.SCREEN_WIDTH / landImage1.getWidth() + 2;
        for (int i = 0; i < numberOfImages; i++) {
            ImageLand imageLand = new ImageLand();
            imageLand.posX = i * landImage1.getWidth();
            imageLand.image = getRandomLandImage();
            landImages.add(imageLand);
        }
    }

    public void update() {
        for (ImageLand imageLand : landImages) {
            imageLand.posX -= GameScreen.globalSpeed;
        }
        ImageLand firstImage = landImages.get(0);
        if (firstImage.posX + landImage1.getWidth() < 0) {
            firstImage.posX = landImages.get(landImages.size() - 1).posX + landImage1.getWidth();
            landImages.add(landImages.remove(0));
        }
    }

    public void draw(Graphics g) {
        for (ImageLand imageLand : landImages) {
            g.drawImage(imageLand.image, (int) imageLand.posX, (int) (GameScreen.GROUND_Y - 20), null);
        }
    }

    private BufferedImage getRandomLandImage() {
        Random random = new Random();
        int i = random.nextInt(3);
        switch (i) {
            case 0:
                return landImage1;
            case 1:
                return landImage2;
            default:
                return landImage3;
        }
    }
}
