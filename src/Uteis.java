import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

public class Uteis {

    private static Random random = new Random();

    public static String getDateFormated(long currentTimeMillis){

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedTime = sdf.format(new Date(currentTimeMillis));
        return formattedTime;
    }

    public static double getRandomValue() {
        return (random.nextInt(20001) / 10.0) - 1000.0;
        //return (random.nextInt(201) / 10.0) - 10.0;
        //return (random.nextInt(2001) / 1000.0) - 1.0;
        //return (random.nextInt(2001) / 10000.0) - 0.1;

        //return random.nextInt(3) - 1;
    }

    public static void salvarRedeArquivo(List<MainCharacter> dinossauros, int melhorScore) {
        double maior = 0;
        int indice = 0;
        for (int i = 0; i < GameScreen.QUANTIDADE_DINOS; i++) {
            if (dinossauros.get(i).fitness > maior) {
                maior = dinossauros.get(i).fitness;
                indice = i;
            }
        }

        String fileName = String.format("redes/%.2f - [%d,%d,%d,%d]",
                melhorScore,
                MainCharacter.QUANTIDADE_ESCONDIDAS_LAYERS,
                MainCharacter.QTD_NEURONIOS_ENTRADA,
                MainCharacter.QTD_NEURONIOS_ESCONDIDA,
                MainCharacter.QTD_NEURONIOS_SAIDA);

        try (FileOutputStream fos = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeInt(dinossauros.get(indice).tamanhoDNA);
            for (double dnaValue : dinossauros.get(indice).DNA) {
                oos.writeDouble(dnaValue);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
