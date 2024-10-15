import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class Utility {

    private static Random random = new Random();

    public static double getRandomValue() {
        return (random.nextDouble() * 2000) - 1000;
    }

    public static String getFormattedDate(long currentTimeMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date(currentTimeMillis));
    }
}
