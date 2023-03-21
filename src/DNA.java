import java.util.List;
import java.util.Random;

public class DNA {

    static final int LARG_GRAFICO = 300;// Set your value here;
    static final int POPULACAO_TAMANHO = 5;// Set your value here;
    static int geracaoCompleta = 0;
    static double[][] DNADaVez = new double[POPULACAO_TAMANHO][];
    static double[] mediaFitnessPopulacao = new double[LARG_GRAFICO];
    static double[] mediaFitnessFilhos = new double[LARG_GRAFICO];
    static double[] bestFitnessPopulacao = new double[LARG_GRAFICO];

    public static double bestFitnessGeracao(List<MainCharacter> dinos) {
        double maior = 0;
        for (int i = 0; i < POPULACAO_TAMANHO; i++) {
            if (dinos.get(i).fitness > maior) {
                maior = dinos.get(i).fitness;
            }
        }
        return maior;
    }

    public static double mediaFitnessGeracao(List<MainCharacter> dinos) {
        double media = 0;
        for (int i = 0; i < POPULACAO_TAMANHO; i++) {
            media = media + dinos.get(i).fitness;
        }
        media = media / (double) POPULACAO_TAMANHO;
        return media;
    }

    public static double bestFitnessEver() {
        double maior = 0;
        for (int i = 0; i < geracaoCompleta; i++) {
            if (bestFitnessPopulacao[i] > maior) {
                maior = bestFitnessPopulacao[i];
            }
        }
        return maior;
    }

    public static void inicializarDNA(List<MainCharacter> dinos, int tamahoDNA) {
        int tamanhoDNA = dinos.get(0).tamanhoDNA;

        for (int i = 0; i < POPULACAO_TAMANHO; i++) {
            DNADaVez[i] = new double[tamanhoDNA];
            for (int j = 0; j < tamanhoDNA; j++) {
                DNADaVez[i][j] = Uteis.getRandomValue();
            }
        }
    }

}

