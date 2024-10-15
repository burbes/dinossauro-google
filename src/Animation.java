import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Animation {
    private List<BufferedImage> frames;
    private int frameIndex;
    private long deltaTime;
    private long lastTime;

    public Animation(int deltaTime) {
        this.deltaTime = deltaTime;
        frames = new ArrayList<>();
        frameIndex = 0;
        lastTime = 0;
    }

    public void updateFrame() {
        if (System.currentTimeMillis() - lastTime >= deltaTime) {
            frameIndex = (frameIndex + 1) % frames.size();
            lastTime = System.currentTimeMillis();
        }
    }

    public void addFrame(BufferedImage frame) {
        frames.add(frame);
    }

    public BufferedImage getFrame() {
        return frames.get(frameIndex);
    }
}
