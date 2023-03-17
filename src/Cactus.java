import java.awt.*;
import java.awt.image.BufferedImage;

public class Cactus extends Enemy {

    private BufferedImage image;
    private int posX, posY;

    private Rectangle rect;
    private MainCharacter mainCharacter;
    private boolean isScoreGot = false;

    public Cactus(MainCharacter mainCharacter) {
        this.mainCharacter = mainCharacter;
        image = Resource.getResourceImage("data/cactus1.png");
        posX = 200;
        posY = 65;
        rect = new Rectangle();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, posX, posY, null);
    }

    @Override
    public void update() {
        posX -= 2;
        rect.x = posX;
        rect.y = posY;
        rect.width = image.getWidth();
        rect.height = image.getHeight();
    }

    @Override
    public Rectangle getBound(){
        return rect;
    }

    public void setX(int x){
        posX = x;
    }
    public void setY(int y){
        posY = y;
    }

    public void setImage(BufferedImage image){
        this.image = image;
    }
    @Override
    public boolean isOutOfScreen() {
        if(posX + image.getWidth() < 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isOver() {
        return mainCharacter.getX() > posX;
    }

    @Override
    public boolean isScoreGot() {
        return isScoreGot;
    }
    @Override
    public void setScoreGot(boolean isScoreGot) {
        this.isScoreGot = isScoreGot;
    }
}
