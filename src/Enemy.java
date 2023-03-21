import java.awt.*;

public abstract class Enemy {

    public abstract void update();
    public abstract void draw(Graphics g);
    public abstract Rectangle getBound();
    public abstract boolean isOutOfScreen();
    public abstract boolean isOver(MainCharacter mainCharacter);
    public abstract boolean isScoreGot();
    public abstract void setScoreGot(boolean isScoreGot);
}
