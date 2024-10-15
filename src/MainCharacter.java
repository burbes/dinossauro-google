import java.awt.*;
import java.awt.image.BufferedImage;

public class MainCharacter {

    private static final int START_X = 50;
    private static final int START_Y = 60;

    // Constantes da Rede Neural
    public static final int INPUT_NEURONS = 6;
    public static final int HIDDEN_NEURONS = 6;
    public static final int OUTPUT_NEURONS = 2;
    public static final int HIDDEN_LAYERS = 1;

    private int dnaSize;
    private int fitness = 0;
    private int score = 0;
    private NeuralNetwork network;
    private Rectangle boundingBox;
    private float x;
    private float y;
    private float speedY;
    private boolean isAlive = true;
    private MainCharacterState state = MainCharacterState.RUNNING;

    // Imagens e animações
    private BufferedImage jumpingImage;
    private BufferedImage deadImage;
    private Animation runningAnimation;
    private Animation duckingAnimation;

    public MainCharacter() {
        runningAnimation = new Animation(90);
        runningAnimation.addFrame(Resource.getImage("data/main-character1.png"));
        runningAnimation.addFrame(Resource.getImage("data/main-character2.png"));
        jumpingImage = Resource.getImage("data/main-character3.png");
        duckingAnimation = new Animation(90);
        duckingAnimation.addFrame(Resource.getImage("data/main-character5.png"));
        duckingAnimation.addFrame(Resource.getImage("data/main-character6.png"));
        deadImage = Resource.getImage("data/main-character4.png");

        x = START_X;
        y = START_Y;

        network = NeuralNetwork.createNetwork(INPUT_NEURONS, HIDDEN_NEURONS, OUTPUT_NEURONS, HIDDEN_LAYERS);
        dnaSize = NeuralNetwork.getWeightCount(network);

        boundingBox = new Rectangle();
    }


    public void update() {
        runningAnimation.updateFrame();
        duckingAnimation.updateFrame();

        if (y >= GameScreen.GROUND_Y - getCharacterHeight()) {
            speedY = 0;
            y = GameScreen.GROUND_Y - getCharacterHeight();
            if (state != MainCharacterState.DUCKING) {
                state = MainCharacterState.RUNNING;
            }
        } else {
            speedY += GameScreen.GRAVITY;
            y += speedY;
        }
        updateBoundingBox();
    }
    private int getCharacterHeight() {
        switch (state) {
            case RUNNING:
                return runningAnimation.getFrame().getHeight();
            case JUMPING:
                return jumpingImage.getHeight();
            case DUCKING:
                return duckingAnimation.getFrame().getHeight();
            default:
                return runningAnimation.getFrame().getHeight();
        }
    }

    private void updateBoundingBox() {
        boundingBox.x = (int) x;
        boundingBox.y = (int) y;
        switch (state) {
            case RUNNING:
                boundingBox.width = runningAnimation.getFrame().getWidth();
                boundingBox.height = runningAnimation.getFrame().getHeight();
                break;
            case JUMPING:
                boundingBox.width = jumpingImage.getWidth();
                boundingBox.height = jumpingImage.getHeight();
                break;
            case DUCKING:
                boundingBox.width = duckingAnimation.getFrame().getWidth();
                boundingBox.height = duckingAnimation.getFrame().getHeight();
                boundingBox.y += 20; // Ajuste se necessário
                break;
            default:
                boundingBox.width = runningAnimation.getFrame().getWidth();
                boundingBox.height = runningAnimation.getFrame().getHeight();
                break;
        }
    }

    public void draw(Graphics g) {
        switch (state) {
            case RUNNING:
                g.drawImage(runningAnimation.getFrame(), (int) x, (int) y, null);
                break;
            case JUMPING:
                g.drawImage(jumpingImage, (int) x, (int) y, null);
                break;
            case DUCKING:
                g.drawImage(duckingAnimation.getFrame(), (int) x, (int) y + 20, null);
                break;
            case DEAD:
                g.drawImage(deadImage, (int) x - 10, (int) y - 10, null);
                break;
        }

    }

    public void jump() {
        if (state != MainCharacterState.JUMPING) {
            speedY = -12f; // Aumente o valor negativo para saltos mais rápidos
            y += speedY;
            state = MainCharacterState.JUMPING;
        }
    }

    public void duck(boolean isDucking) {
        if (state == MainCharacterState.JUMPING) {
            return;
        }
        if (isDucking) {
            state = MainCharacterState.DUCKING;
        } else {
            state = MainCharacterState.RUNNING;
        }
    }

    public void increaseScore() {
        this.score += 20;
        if (this.score % 1000 == 0) {
            this.fitness++;
        }
    }

    public void die() {
        state = MainCharacterState.DEAD;
        isAlive = false;
    }

    public void reset() {
        isAlive = true;
        state = MainCharacterState.RUNNING;
        score = 0;
        fitness = 0;
        x = START_X;
        y = START_Y;
        speedY = 0;
        boundingBox = new Rectangle();
        updateBoundingBox();
    }

    // Getters e Setters
    public boolean isAlive() {
        return isAlive;
    }

    public int getScore() {
        return score;
    }

    public int getFitness() {
        return score;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public NeuralNetwork getBrain() {
        return network;
    }

    public void setBrain(NeuralNetwork network) {
        this.network = network;
    }

    public int getDNASize() {
        return dnaSize;
    }

    public float getX() {
        return x;
    }

}
