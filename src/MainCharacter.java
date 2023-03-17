
import java.awt.*;
import java.awt.image.BufferedImage;

public class MainCharacter {

    private int TamanhoDNA;
    private double DNA;

    private static final int NORMAL_RUN = 0;
    private static final int JUMPING = 1;
    private static final int DOWN_RUN = 2;
    private static final int DEATH = 3;


    private float x = 0;
    private float y = 0;
    private float speedY = 0;
    private float speedX = 0;
    private Rectangle rect;
    public int score = 0;
    private int state = NORMAL_RUN;

    private BufferedImage jumping;
    private BufferedImage deathImage;

    private Animation normalRunAnim;

    private Animation downRunAnim;

    private boolean isAlive = true;


    public MainCharacter(){
        normalRunAnim = new Animation(90);
        normalRunAnim.addFrame(Resource.getResourceImage("data/main-character1.png"));
        normalRunAnim.addFrame(Resource.getResourceImage("data/main-character2.png"));
        jumping = Resource.getResourceImage("data/main-character3.png");
        downRunAnim = new Animation(90);
        downRunAnim.addFrame(Resource.getResourceImage("data/main-character5.png"));
        downRunAnim.addFrame(Resource.getResourceImage("data/main-character6.png"));
        deathImage = Resource.getResourceImage("data/main-character4.png");

        rect = new Rectangle();


    }

    public void update(){
        normalRunAnim.updateFrame();
        downRunAnim.updateFrame();
        //all these line code for jumping
        if(y >= GameScreen.GROUNDy - normalRunAnim.getFrame().getHeight()){ //  para subtrair o tamanho do personagem
            speedY = 0;
            y = GameScreen.GROUNDy -  normalRunAnim.getFrame().getHeight();
            if(state != DOWN_RUN) {
                state = NORMAL_RUN;
            }
        } else {
            speedY+=GameScreen.GRAVITY;
            y+=speedY;
        }
        rect.x = (int) x;
        rect.y = (int) y;
        rect.width = normalRunAnim.getFrame().getWidth();
        rect.height = normalRunAnim.getFrame().getHeight();

    }


    public Rectangle getBound() {
        return rect;
    }

    public void draw(Graphics g){
        switch(state) {
            case NORMAL_RUN:
                g.drawImage(normalRunAnim.getFrame(), (int) x, (int) y, null);
                break;
            case JUMPING:
                g.drawImage(jumping, (int) x, (int) y, null);
                break;
            case DOWN_RUN:
                g.drawImage(downRunAnim.getFrame(), (int) x, (int) (y + 20), null);
                break;
            case DEATH:
                g.drawImage(deathImage, (int) x, (int) y, null);
                break;
        }
    }

    public void jump(){

        speedY = -4f;
        y += speedY;
        state = JUMPING;
    }

    public void down(boolean isDown) {
        if(state == JUMPING) {
            return;
        }
        if(isDown) {
            state = DOWN_RUN;
        } else {
            state = NORMAL_RUN;
        }
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
    public void upScore() {
        score += 20;
        if(score % 100 == 0) {
            //scoreUpSound.play();
        }
    }

    public void dead(boolean isDeath) {
        if(isDeath) {
            state = DEATH;
        } else {
            state = NORMAL_RUN;
        }
    }
}
