import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DNA {

    static final int LARG_GRAFICO = 300;// Set your value here;

    static int geracaoCompleta = 0;
    static double[][] DNADaVez = new double[GameScreen.QUANTIDADE_DINOS][];
    static double[] mediaFitnessPopulacao = new double[LARG_GRAFICO];
    static double[] mediaFitnessFilhos = new double[LARG_GRAFICO];
    static double[] bestFitnessPopulacao = new double[LARG_GRAFICO];

    public static double bestFitnessGeracao(List<MainCharacter> dinos) {
        double maior = 0;
        for (int i = 0; i < GameScreen.QUANTIDADE_DINOS; i++) {
            if (dinos.get(i).fitness > maior) {
                maior = dinos.get(i).fitness;
            }
        }
        return maior;
    }

    public static double mediaFitnessGeracao(List<MainCharacter> dinos) {
        double media = 0;
        for (int i = 0; i < GameScreen.QUANTIDADE_DINOS; i++) {
            media = media + dinos.get(i).fitness;
        }
        media = media / (double) GameScreen.QUANTIDADE_DINOS;
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

    public static void inicializarDNA(List<MainCharacter> dinos) {
        int tamanhoDNA = dinos.get(0).tamanhoDNA;

        for (int i = 0; i < GameScreen.QUANTIDADE_DINOS; i++) {
            DNADaVez[i] = new double[tamanhoDNA];
            for (int j = 0; j < tamanhoDNA; j++) {
                DNADaVez[i][j] = Uteis.getRandomValue();
            }
        }
    }


    public static List<MainCharacter> randomMutations(List<MainCharacter> dinossauros, MainCharacter[] os2melhorDino) {
        double rangeRandom = os2melhorDino[0].tamanhoDNA;

        MainCharacter[] vetor = new MainCharacter[GameScreen.QUANTIDADE_DINOS];

        //vetor com clones do melhor dino
        for (int i = 0; i < GameScreen.QUANTIDADE_DINOS; i++) {
            MainCharacter aux = new MainCharacter();

            if(new Random().nextBoolean()){
                aux.cerebro = os2melhorDino[0].cerebro;
            }else{
                aux.cerebro = os2melhorDino[1].cerebro;
            }
            aux.setAlive(true);
            aux.dead(false);
            aux.score = 0;
            vetor[i] = aux;
        }

        //criando variações do melhor dino (pula o primeiro para deixar identico)
        for (int j = 1; j < GameScreen.QUANTIDADE_DINOS; j++) {
            int tipo;
            int mutations = (int) (Math.random() * rangeRandom) + 1;

            for (int k = 0; k < mutations; k++) {
                tipo = (int) (Math.random() * 3);

                int indice = (int) (Math.random() * vetor[j].tamanhoDNA);



                    vetor[j].DNA[indice] = Uteis.getRandomValue();

//
//
//                switch (tipo) {
//                    case 0:
//                        vetor[j].DNA[indice] = Uteis.getRandomValue();
//                        break;
//                    case 1:
//                        double number = (Math.random() * 10001) / 10000.0 + 0.5;
//                        vetor[j].DNA[indice] = vetor[j].DNA[indice] * number;
//                        break;
//                    case 2:
//                        double number2 = Uteis.getRandomValue() / 100.0;
//                        vetor[j].DNA[indice] = vetor[j].DNA[indice] + number2;
//                        break;
//                }
            }
        }

        for (int j = 0; j < GameScreen.QUANTIDADE_DINOS; j++) {
            for (int k = 0; k < dinossauros.get(j).tamanhoDNA; k++) {
                DNADaVez[j][k] = dinossauros.get(j).DNA[k];
            }
        }

        return Arrays.stream(vetor).toList();

    }


}

