import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Pterodactyl extends Enemy {

    private float posX, posY;

    private Rectangle rect;

    private Animation flyingAnim;
    private boolean isScoreGot = false;

    private Random rand;
    public Pterodactyl() {

        rand = new Random();
        flyingAnim = new Animation(90);
        flyingAnim.addFrame(Resource.getResourceImage("data/pterodacty.jpg"));
        flyingAnim.addFrame(Resource.getResourceImage("data/pterodacty2.jpg"));

        posX = GameWindow.SCREEN_WIDTH - 100;
        posY = getRandomNumberBetween0And150();
        rect = new Rectangle();
    }
    public int getRandomNumberBetween0And150() {
        return rand.nextInt(101);
    }
    @Override
    public void draw(Graphics g) {
        g.drawImage(flyingAnim.getFrame(), (int)posX, (int)posY, null);
    }

    @Override
    public void update() {
        flyingAnim.updateFrame();
        posX -=15;

        rect.x = (int)posX;
        rect.y = (int)posY;
        rect.width = flyingAnim.getFrame().getWidth();
        rect.height = flyingAnim.getFrame().getHeight();
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

    @Override
    public boolean isOutOfScreen() {
        if(posX < 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isOver(MainCharacter mainCharacter) {
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
