
import enums.MainCharacterStateEnum;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class MainCharacter implements Cloneable {

    public static final int QUANTIDADE_ESCONDIDAS_LAYERS = 1;
    public static final int QTD_NEURONIOS_ENTRADA = 6;
    public static final int QTD_NEURONIOS_ESCONDIDA = 6;
    public static final int QTD_NEURONIOS_SAIDA = 2;
    public int tamanhoDNA;
    public double[] DNA;
    public double fitness;

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

    private Color color; //TODO

    public MainCharacter(){
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
        DNA = new double[tamanhoDNA];

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

    private Color getRandomColor(){
        Random rand = new Random(3);
        Color color =Color.GRAY;
        if(rand.nextInt() == 0){
            color=  Color.BLACK;
        }
        if(rand.nextInt() == 1){
            color=  Color.BLUE;
        }
        if(rand.nextInt() == 2){
            color=  Color.RED;
        }
        if(rand.nextInt() == 3){
            color=  Color.GREEN;
        }
        return color;
    }

    public void update(){
        normalRunAnim.updateFrame();
        downRunAnim.updateFrame();
        //all these line code for jumping
        if(y >= GameScreen.GROUNDy - normalRunAnim.getFrame().getHeight()){ //  para subtrair o tamanho do personagem
            speedY = 0;
            y = GameScreen.GROUNDy -  normalRunAnim.getFrame().getHeight();
            if(state != MainCharacterStateEnum.DOWN_RUN) {
                state = MainCharacterStateEnum.NORMAL_RUN;
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
                g.drawImage(deathImage, (int) x-10, (int) y-10, null);
                break;
        }
    }

    public void jump(){
        if(state != MainCharacterStateEnum.JUMPING){
            speedY = -10f;
            y += speedY;
            state = MainCharacterStateEnum.JUMPING;
        }
    }

    public void down(boolean isDown) {
        if(state == MainCharacterStateEnum.JUMPING) {
            return;
        }
        if(isDown) {
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
        score += 20;
        if(score % 1000 == 0) {
            scoreUpSound.play();
        }
    }

    public int getScore(){
        return score;
    }

    public void dead(boolean isDeath) {
        if(isDeath) {
            state = MainCharacterStateEnum.DEATH;
        } else {
            state = MainCharacterStateEnum.NORMAL_RUN;
        }
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        MainCharacter that = (MainCharacter) o;
//        return tamanhoDNA == that.tamanhoDNA && Double.compare(that.fitness, fitness) == 0 && Float.compare(that.x, x) == 0 && Float.compare(that.y, y) == 0 && Float.compare(that.speedY, speedY) == 0 && Float.compare(that.speedX, speedX) == 0 && score == that.score && isAlive == that.isAlive && Arrays.equals(DNA, that.DNA) && Objects.equals(cerebro, that.cerebro) && Objects.equals(rect, that.rect) && state == that.state && Objects.equals(jumping, that.jumping) && Objects.equals(deathImage, that.deathImage) && Objects.equals(normalRunAnim, that.normalRunAnim) && Objects.equals(downRunAnim, that.downRunAnim) && Objects.equals(jumpSound, that.jumpSound) && Objects.equals(deadSound, that.deadSound) && Objects.equals(scoreUpSound, that.scoreUpSound) && Objects.equals(color, that.color);
//    }
//
//    @Override
//    public int hashCode() {
//        int result = Objects.hash(tamanhoDNA, fitness, cerebro, x, y, speedY, speedX, rect, score, state, jumping, deathImage, normalRunAnim, downRunAnim, isAlive, jumpSound, deadSound, scoreUpSound, color);
//        result = 31 * result + Arrays.hashCode(DNA);
//        return result;
//    }


    @Override
    public MainCharacter clone() {
        try {
            return (MainCharacter) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning not supported", e);
        }
    }
}
