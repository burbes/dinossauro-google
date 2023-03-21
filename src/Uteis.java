import java.util.Random;

public class Uteis {


    private static Random random = new Random();

    public static double getRandomValue() {
        return (random.nextInt(20001) / 10.0) - 1000.0;
        //return (random.nextInt(201) / 10.0) - 10.0;
        //return (random.nextInt(2001) / 1000.0) - 1.0;
        //return (random.nextInt(2001) / 10000.0) - 0.1;

        //return random.nextInt(3) - 1;
    }
}
