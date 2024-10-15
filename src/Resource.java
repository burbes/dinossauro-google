import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Resource {
    public static BufferedImage getImage(String path) {
        try {
            return ImageIO.read(new File(path)); // Removido "resources/" do caminho
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

