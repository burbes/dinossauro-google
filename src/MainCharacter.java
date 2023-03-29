import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class MainCharacter {

    public static final int QUANTIDADE_ESCONDIDAS_LAYERS = 1;
    public static final int QTD_NEURONIOS_ENTRADA = 6;
    public static final int QTD_NEURONIOS_ESCONDIDA = 6;
    public static final int QTD_NEURONIOS_SAIDA = 2;

    int tamanhoDNA;
    int fitness = 0;

    NeuralNetwork.RedeNeural cerebro;

    private float x = 0;
    private float y = 0;
    private float speedY = 0;
    private float speedX = 0;
    private Rectangle rect;
    public int score = 0;
    private MainCharacterStateEnum state = MainCharacterStateEnum.NORMAL_RUN;

    private BufferedImage jumping;
    private BufferedImage deathImage;

    private Animation normalRunAnim;

    private Animation downRunAnim;

    private boolean isAlive = true;

    @SuppressWarnings("unchecked")
    private AudioClip jumpSound;
    @SuppressWarnings("unchecked")
    private AudioClip deadSound;
    @SuppressWarnings("unchecked")
    private AudioClip scoreUpSound;

    private Color color;

    public MainCharacter() {
        normalRunAnim = new Animation(90);
        normalRunAnim.addFrame(Resource.getResourceImage("data/main-character1.png"));
        normalRunAnim.addFrame(Resource.getResourceImage("data/main-character2.png"));
        jumping = Resource.getResourceImage("data/main-character3.png");
        downRunAnim = new Animation(90);
        downRunAnim.addFrame(Resource.getResourceImage("data/main-character5.png"));
        downRunAnim.addFrame(Resource.getResourceImage("data/main-character6.png"));
        deathImage = Resource.getResourceImage("data/main-character4.png");

        setX(50); //empurra o dino um pouco pra frente
        setY(60);

        cerebro = NeuralNetwork.RNA_CriarRedeNeural(QUANTIDADE_ESCONDIDAS_LAYERS, QTD_NEURONIOS_ENTRADA, QTD_NEURONIOS_ESCONDIDA, QTD_NEURONIOS_SAIDA);
        tamanhoDNA = NeuralNetwork.RNA_QuantidadePesos(cerebro);

        rect = new Rectangle();

        color = getRandomColor();
        try {
            jumpSound = Applet.newAudioClip(new URL("file", "", "data/jump.wav"));
            deadSound = Applet.newAudioClip(new URL("file", "", "data/dead.wav"));
            scoreUpSound = Applet.newAudioClip(new URL("file", "", "data/scoreup.wav"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private Color getRandomColor() {
        Random rand = new Random(3);
        Color color = Color.GRAY;
        if (rand.nextInt() == 0) {
            color = Color.BLACK;
        }
        if (rand.nextInt() == 1) {
            color = Color.BLUE;
        }
        if (rand.nextInt() == 2) {
            color = Color.RED;
        }
        if (rand.nextInt() == 3) {
            color = Color.GREEN;
        }
        return color;
    }

    public void update() {
        normalRunAnim.updateFrame();
        downRunAnim.updateFrame();
        //all these line code for jumping
        if (y >= GameScreen.GROUNDy - normalRunAnim.getFrame().getHeight()) { //  para subtrair o tamanho do personagem
            speedY = 0;
            y = GameScreen.GROUNDy - normalRunAnim.getFrame().getHeight();
            if (state != MainCharacterStateEnum.DOWN_RUN) {
                state = MainCharacterStateEnum.NORMAL_RUN;
            }
        } else {
            speedY += GameScreen.GRAVITY;
            y += speedY;
        }
        rect.x = (int) x;
        rect.y = (int) y;
        rect.width = normalRunAnim.getFrame().getWidth();
        rect.height = normalRunAnim.getFrame().getHeight();
    }

    public Rectangle getBound() {
        return rect;
    }

    public void draw(Graphics g) {
        switch (state) {
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
                g.drawImage(deathImage, (int) x - 10, (int) y - 10, null);
                break;
        }
    }

    public void jump() {
        if (state != MainCharacterStateEnum.JUMPING) {
            speedY = -10f;
            y += speedY;
            state = MainCharacterStateEnum.JUMPING;
        }
    }

    public void down(boolean isDown) {
        if (state == MainCharacterStateEnum.JUMPING) {
            return;
        }
        if (isDown) {
            state = MainCharacterStateEnum.DOWN_RUN;
        } else {
            state = MainCharacterStateEnum.NORMAL_RUN;
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
        this.score += 20;
        if (this.score % 1000 == 0) {
            scoreUpSound.play();
            this.fitness++;
        }
    }

    public int getScore() {
        return score;
    }

    public int getFitness() {
        return fitness;
    }

    public void dead(boolean isDeath) {
        if (isDeath) {
            state = MainCharacterStateEnum.DEATH;
        } else {
            state = MainCharacterStateEnum.NORMAL_RUN;
        }
    }

}
