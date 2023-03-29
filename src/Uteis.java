import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class Uteis {


    private static Random random = new Random();

    public static double getRandomValue() {
        return (random.nextInt(20001) / 10.0) - 1000.0;
    }

    public static String getDateFormated(long currentTimeMillis){

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedTime = sdf.format(new Date(currentTimeMillis));
        return formattedTime;
    }
}
