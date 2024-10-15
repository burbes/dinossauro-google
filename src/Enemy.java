import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Enemy {

    protected int posX;
    protected int posY;
    protected Rectangle boundingBox;
    protected boolean scoreCounted;

    public Enemy() {
        boundingBox = new Rectangle();
        scoreCounted = false;
    }

    public abstract void update();

    public abstract void draw(Graphics g);

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public boolean isOffScreen() {
        return posX + boundingBox.width < 0;
    }

    public boolean isCollidingWith(MainCharacter character) {
        return character.getBoundingBox().intersects(boundingBox);
    }

    public boolean isPassedBy(MainCharacter character) {
        return character.getX() > posX && !scoreCounted;
    }

    public boolean isScoreCounted() {
        return scoreCounted;
    }

    public void setScoreCounted(boolean scoreCounted) {
        this.scoreCounted = scoreCounted;
    }
}
