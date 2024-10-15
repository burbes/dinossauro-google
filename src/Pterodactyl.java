import java.awt.Graphics;
import java.util.Random;

public class Pterodactyl extends Enemy {

    private Animation flyingAnimation;

    public Pterodactyl() {
        flyingAnimation = new Animation(90);
        flyingAnimation.addFrame(Resource.getImage("data/pterodactyl1.jpg"));
        flyingAnimation.addFrame(Resource.getImage("data/pterodactyl2.jpg"));

        posX = GameScreen.SCREEN_WIDTH;
        Random random = new Random();
        posY = random.nextInt(50) + 50; // Posição Y aleatória

        boundingBox.width = flyingAnimation.getFrame().getWidth();
        boundingBox.height = flyingAnimation.getFrame().getHeight();
    }

    @Override
    public void update() {
        flyingAnimation.updateFrame();
        posX -= GameScreen.globalSpeed;
        boundingBox.x = posX;
        boundingBox.y = posY;
    }


    @Override
    public void draw(Graphics g) {
        g.drawImage(flyingAnimation.getFrame(), posX, posY, null);
    }
}
