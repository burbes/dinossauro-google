import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;

public class Cactus extends Enemy {

    private BufferedImage image;

    public Cactus(BufferedImage image1, BufferedImage image2) {
        image = Math.random() < 0.5 ? image1 : image2;
        posX = GameScreen.SCREEN_WIDTH;
        posY = (int) (GameScreen.GROUND_Y - image.getHeight());
        boundingBox = new Rectangle();
        boundingBox.x = posX;
        boundingBox.y = posY;
        boundingBox.width = image.getWidth();
        boundingBox.height = image.getHeight();
    }

    @Override
    public void update() {
        posX -= GameScreen.globalSpeed;
        boundingBox.x = posX;
        boundingBox.y = posY;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, posX, posY, null);
    }
}
