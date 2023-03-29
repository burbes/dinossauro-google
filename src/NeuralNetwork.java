import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

public class NeuralNetwork {

    static final double TAXA_APRENDIZADO = 0.1;
    static final int BIAS = 1;

    static class Neuronio {
        double[] Peso = new double[]{};
        double Erro = 0.0;
        double Saida = 1.0;
        int QuantidadeLigacoes = 0;
    }

    static class Camada {
        Neuronio[] Neuronios = new Neuronio[]{};
        int QuantidadeNeuronios;
    }

    static class RedeNeural {
        Camada CamadaEntrada = new Camada();
        Camada[] CamadaEscondida = new Camada[]{};
        Camada CamadaSaida = new Camada();
        int QuantidadeEscondidas = 0;
    }

    static double relu(double X) {
        if (X < 0) {
            return 0;
        } else {
            return X;
        }
    }

    static double reluDx(double X) {
        if (X < 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Deprecated(since = "i'm not using, another aproach")
    static void RNA_CopiarVetorParaCamadas(RedeNeural Rede, double[] Vetor) {
        int j, k, l;

        j = 0;

        for (int i = 0; i < Rede.QuantidadeEscondidas; i++) {
            for (k = 0; k < Rede.CamadaEscondida[i].QuantidadeNeuronios; k++) {
                for (l = 0; l < Rede.CamadaEscondida[i].Neuronios[k].QuantidadeLigacoes; l++) {
                    Rede.CamadaEscondida[i].Neuronios[k].Peso[l] = Vetor[j];
                    j++;
                }
            }
        }

        for (k = 0; k < Rede.CamadaSaida.QuantidadeNeuronios; k++) {
            for (l = 0; l < Rede.CamadaSaida.Neuronios[k].QuantidadeLigacoes; l++) {
                Rede.CamadaSaida.Neuronios[k].Peso[l] = Vetor[j];
                j++;
            }
        }
    }

    static void RNA_CopiarParaEntrada(RedeNeural Rede, double[] VetorEntrada) {
        int i;

        for (i = 0; i < Rede.CamadaEntrada.QuantidadeNeuronios - BIAS; i++) {
            Rede.CamadaEntrada.Neuronios[i].Saida = VetorEntrada[i];
        }
    }

    static int RNA_QuantidadePesos(RedeNeural Rede) {
        int Soma = 0;
        for (int i = 0; i < Rede.QuantidadeEscondidas; i++) {
            for (int j = 0; j < Rede.CamadaEscondida[i].QuantidadeNeuronios; j++) {
                Soma = Soma + Rede.CamadaEscondida[i].Neuronios[j].QuantidadeLigacoes;
            }
        }

        for (int i = 0; i < Rede.CamadaSaida.QuantidadeNeuronios; i++) {
            Soma = Soma + Rede.CamadaSaida.Neuronios[i].QuantidadeLigacoes;
        }
        return Soma;
    }

    static void RNA_CopiarDaSaida(RedeNeural Rede, double[] VetorSaida) {
        int i;

        for (i = 0; i < Rede.CamadaSaida.QuantidadeNeuronios; i++) {
            VetorSaida[i] = Rede.CamadaSaida.Neuronios[i].Saida;
        }
    }


    static void RNA_CalcularSaida(RedeNeural Rede) {
        int i, j, k;
        double Somatorio;

        for (i = 0; i < Rede.CamadaEscondida[0].QuantidadeNeuronios - BIAS; i++) {
            Somatorio = 0;
            for (j = 0; j < Rede.CamadaEntrada.QuantidadeNeuronios; j++) {
                Somatorio = Somatorio + Rede.CamadaEntrada.Neuronios[j].Saida * Rede.CamadaEscondida[0].Neuronios[i].Peso[j];
            }
            Rede.CamadaEscondida[0].Neuronios[i].Saida = relu(Somatorio);
        }

        for (k = 1; k < Rede.QuantidadeEscondidas; k++) {
            for (i = 0; i < Rede.CamadaEscondida[k].QuantidadeNeuronios - BIAS; i++) {
                Somatorio = 0;
                for (j = 0; j < Rede.CamadaEscondida[k - 1].QuantidadeNeuronios; j++) {
                    Somatorio = Somatorio + Rede.CamadaEscondida[k - 1].Neuronios[j].Saida * Rede.CamadaEscondida[k].Neuronios[i].Peso[j];
                }
                Rede.CamadaEscondida[k].Neuronios[i].Saida = relu(Somatorio);
            }
        }

        for (i = 0; i < Rede.CamadaSaida.QuantidadeNeuronios; i++) {
            Somatorio = 0;
            if (Rede.QuantidadeEscondidas - 1 == -1) {
                System.out.println("erro aqui");
            }

            for (j = 0; j < Rede.CamadaEscondida[Rede.QuantidadeEscondidas - 1].QuantidadeNeuronios; j++) {
                Somatorio = Somatorio + Rede.CamadaEscondida[Rede.QuantidadeEscondidas - 1].Neuronios[j].Saida * Rede.CamadaSaida.Neuronios[i].Peso[j];
            }

            Rede.CamadaSaida.Neuronios[i].Saida = relu(Somatorio);
        }
    }

    static void RNA_CriarNeuronio(Neuronio Neuron, int QuantidadeLigacoes) {

        Neuron.QuantidadeLigacoes = QuantidadeLigacoes;
        Neuron.Peso = new double[QuantidadeLigacoes];

        for (int i = 0; i < QuantidadeLigacoes; i++) {
            Neuron.Peso[i] = (int) (Math.random() * 2000) - 1000;
        }

        Neuron.Erro = 0;
        Neuron.Saida = 1;
    }

    static RedeNeural RNA_CriarRedeNeural(int QuantidadeEscondidas, int QtdNeuroniosEntrada, int QtdNeuroniosEscondida, int QtdNeuroniosSaida) {
        int i, j;

        QtdNeuroniosEntrada = QtdNeuroniosEntrada + BIAS;
        QtdNeuroniosEscondida = QtdNeuroniosEscondida + BIAS;

        RedeNeural Rede = new RedeNeural();

        Rede.CamadaEntrada.QuantidadeNeuronios = QtdNeuroniosEntrada;
        Rede.CamadaEntrada.Neuronios = new Neuronio[QtdNeuroniosEntrada];

        for (i = 0; i < QtdNeuroniosEntrada; i++) {
            Rede.CamadaEntrada.Neuronios[i] = new Neuronio();
            Rede.CamadaEntrada.Neuronios[i].Saida = 1.0;
        }

        Rede.QuantidadeEscondidas = QuantidadeEscondidas;
        Rede.CamadaEscondida = new Camada[QuantidadeEscondidas];

        for (i = 0; i < QuantidadeEscondidas; i++) {
            Rede.CamadaEscondida[i] = new Camada();
            Rede.CamadaEscondida[i].QuantidadeNeuronios = QtdNeuroniosEscondida;
            Rede.CamadaEscondida[i].Neuronios = new Neuronio[QtdNeuroniosEscondida];

            for (j = 0; j < QtdNeuroniosEscondida; j++) {
                Rede.CamadaEscondida[i].Neuronios[j] = new Neuronio();
                if (i == 0) {
                    RNA_CriarNeuronio(Rede.CamadaEscondida[i].Neuronios[j], QtdNeuroniosEntrada);
                } else {
                    RNA_CriarNeuronio(Rede.CamadaEscondida[i].Neuronios[j], QtdNeuroniosEscondida);
                }
            }
        }

        Rede.CamadaSaida.QuantidadeNeuronios = QtdNeuroniosSaida;
        Rede.CamadaSaida.Neuronios = new Neuronio[QtdNeuroniosSaida];

        for (j = 0; j < QtdNeuroniosSaida; j++) {
            Rede.CamadaSaida.Neuronios[j] = new Neuronio();

            RNA_CriarNeuronio(Rede.CamadaSaida.Neuronios[j], QtdNeuroniosEscondida);
        }

        return Rede;
    }
    public static void RNA_SalvarRede(RedeNeural rede, int geracao, String startTime) {

        DecimalFormat df = new DecimalFormat("###,##0.00;###,##0.00");
        System.setProperty("file.encoding", "UTF-8");
        String fileName = "resources/RedeNeural-" + startTime+".txt";
        File file = new File(fileName);

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
            // Escreve informações sobre a rede neural
            if (geracao == 1) {
                writer.write("Quantidade de camadas escondidas: " + rede.QuantidadeEscondidas + "\n");
                writer.write("Quantidade de neuronios na camada de entrada: " + rede.CamadaEntrada.QuantidadeNeuronios + "\n");
                writer.write("Quantidade de neuronios na primeira camada escondida: " + rede.CamadaEscondida[0].QuantidadeNeuronios + "\n");
                writer.write("Quantidade de neuronios na camada de saida: " + rede.CamadaSaida.QuantidadeNeuronios + "\n");
                writer.write("\n");
            }

            // Escreve informações sobre as camadas escondidas
            writer.write("GERAÇÃO: " + geracao + "\n");
            for (int k = 0; k < rede.QuantidadeEscondidas; k++) {
                writer.write("Camada escondida " + (k+1) + ":\n");
                for (int i = 0; i < rede.CamadaEscondida[k].QuantidadeNeuronios; i++) {
                    writer.write("\tNeuronio " + (i+1) + ":\n");
                    for (int j = 0; j < rede.CamadaEscondida[k].Neuronios[i].QuantidadeLigacoes; j++) {
                        writer.write("\t\tPeso " + (j+1) + ": " +  df.format(rede.CamadaEscondida[k].Neuronios[i].Peso[j]) + "\t");
                    }
                    writer.write("\n");
                }
            }

            // Escreve informações sobre a camada de saída
            writer.write("Camada de saida:\n");
            for (int i = 0; i < rede.CamadaSaida.QuantidadeNeuronios; i++) {
                writer.write("\tNeuronio " + (i+1) + ":\n");
                for (int j = 0; j < rede.CamadaSaida.Neuronios[i].QuantidadeLigacoes; j++) {
                    writer.write("\t\tPeso " + (j+1) + ": " +  df.format(rede.CamadaSaida.Neuronios[i].Peso[j]) + "\t");
                }
                writer.write("\n");
            }

            // Escreve separador
            writer.write("\n=====================\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Deprecated(since = "another approach, this is not in use")
    static void calculateError(RedeNeural redeNeural, double[] target) {
        int i, j, k;
        double sum;

        for (i = 0; i < redeNeural.CamadaSaida.QuantidadeNeuronios; i++) {
            redeNeural.CamadaSaida.Neuronios[i].Erro = (target[i] - redeNeural.CamadaSaida.Neuronios[i].Saida) * reluDx(redeNeural.CamadaSaida.Neuronios[i].Saida);
        }

        for (k = redeNeural.QuantidadeEscondidas - 1; k >= 0; k--) {
            for (i = 0; i < redeNeural.CamadaEscondida[k].QuantidadeNeuronios - BIAS; i++) {
                sum = 0;
                if (k == redeNeural.CamadaEscondida.length - 1) {
                    for (j = 0; j < redeNeural.CamadaSaida.QuantidadeNeuronios; j++) {
                        sum += redeNeural.CamadaSaida.Neuronios[j].Peso[i] * redeNeural.CamadaSaida.Neuronios[j].Erro;
                    }
                } else {
                    for (j = 0; j < redeNeural.CamadaEscondida[k + 1].QuantidadeNeuronios - BIAS; j++) {
                        sum += redeNeural.CamadaEscondida[k + 1].Neuronios[j].Peso[i] * redeNeural.CamadaEscondida[k + 1].Neuronios[j].Erro;
                    }
                }
                redeNeural.CamadaEscondida[k].Neuronios[i].Erro = reluDx(redeNeural.CamadaEscondida[k].Neuronios[i].Saida) * sum;
            }
        }
    }


    @Deprecated(since = "another approach, this is not in use")
    static void updateWeights(RedeNeural redeNeural) {
        int i, j, k;

        for (k = 0; k < redeNeural.QuantidadeEscondidas; k++) {
            for (i = 0; i < redeNeural.CamadaEscondida[k].QuantidadeNeuronios - BIAS; i++) {
                for (j = 0; j < redeNeural.CamadaEscondida[k].Neuronios[i].QuantidadeLigacoes; j++) {
                    if (k == 0) {
                        redeNeural.CamadaEscondida[k].Neuronios[i].Peso[j] += TAXA_APRENDIZADO * redeNeural.CamadaEntrada.Neuronios[j].Saida * redeNeural.CamadaEscondida[k].Neuronios[i].Erro;
                    } else {
                        redeNeural.CamadaEscondida[k].Neuronios[i].Peso[j] += TAXA_APRENDIZADO * redeNeural.CamadaEscondida[k - 1].Neuronios[j].Saida * redeNeural.CamadaEscondida[k].Neuronios[i].Erro;
                    }
                }
            }
        }

        for (i = 0; i < redeNeural.CamadaSaida.QuantidadeNeuronios; i++) {
            for (j = 0; j < redeNeural.CamadaSaida.Neuronios[i].QuantidadeLigacoes; j++) {
                redeNeural.CamadaSaida.Neuronios[i].Peso[j] += TAXA_APRENDIZADO * redeNeural.CamadaEscondida[redeNeural.QuantidadeEscondidas - 1].Neuronios[j].Saida * redeNeural.CamadaSaida.Neuronios[i].Erro;
            }
        }
    }


    static RedeNeural crossoverAndMutateRedeNeural(RedeNeural parent1, RedeNeural parent2, double mutationRate) {
        RedeNeural child = new RedeNeural();

        // Copia a Camada de Entrada do primeiro parent
        child.CamadaEntrada = parent1.CamadaEntrada;
        child.QuantidadeEscondidas = parent1.QuantidadeEscondidas;

        // Crossover e mutação para a Camada Escondida
        child.CamadaEscondida = new Camada[parent1.QuantidadeEscondidas];
        for (int k = 0; k < parent1.QuantidadeEscondidas; k++) {
            child.CamadaEscondida[k] = new Camada();
            child.CamadaEscondida[k].QuantidadeNeuronios = parent1.CamadaEscondida[k].QuantidadeNeuronios;
            child.CamadaEscondida[k].Neuronios = new Neuronio[child.CamadaEscondida[k].QuantidadeNeuronios];

            for (int i = 0; i < child.CamadaEscondida[k].QuantidadeNeuronios; i++) {
                child.CamadaEscondida[k].Neuronios[i] = new Neuronio();
                child.CamadaEscondida[k].Neuronios[i].QuantidadeLigacoes = parent1.CamadaEscondida[k].Neuronios[i].QuantidadeLigacoes;
                child.CamadaEscondida[k].Neuronios[i].Peso = new double[child.CamadaEscondida[k].Neuronios[i].QuantidadeLigacoes];

                for (int j = 0; j < child.CamadaEscondida[k].Neuronios[i].QuantidadeLigacoes; j++) {
                    double rand = Math.random();
                    if (rand < mutationRate/100) {
                        child.CamadaEscondida[k].Neuronios[i].Peso[j] = Uteis.getRandomValue(); // Função para gerar um peso aleatório
                    } else {
                        child.CamadaEscondida[k].Neuronios[i].Peso[j] = Math.random() < 0.5 ? parent1.CamadaEscondida[k].Neuronios[i].Peso[j] : parent2.CamadaEscondida[k].Neuronios[i].Peso[j];
                    }
                }
            }
        }
        // Crossover e mutação para a Camada de Saída
        child.CamadaSaida.QuantidadeNeuronios = parent1.CamadaSaida.QuantidadeNeuronios;
        child.CamadaSaida.Neuronios = new Neuronio[child.CamadaSaida.QuantidadeNeuronios];

        for (int i = 0; i < child.CamadaSaida.QuantidadeNeuronios; i++) {
            child.CamadaSaida.Neuronios[i] = new Neuronio();
            child.CamadaSaida.Neuronios[i].QuantidadeLigacoes = parent1.CamadaSaida.Neuronios[i].QuantidadeLigacoes;
            child.CamadaSaida.Neuronios[i].Peso = new double[child.CamadaSaida.Neuronios[i].QuantidadeLigacoes];

            for (int j = 0; j < child.CamadaSaida.Neuronios[i].QuantidadeLigacoes; j++) {
                double rand = Math.random();
                if (rand < mutationRate/100) {
                    child.CamadaSaida.Neuronios[i].Peso[j] = Uteis.getRandomValue(); // Função para gerar um peso aleatório
                } else {
                    child.CamadaSaida.Neuronios[i].Peso[j] = Math.random() < 0.5 ? parent1.CamadaSaida.Neuronios[i].Peso[j] : parent2.CamadaSaida.Neuronios[i].Peso[j];
                }
            }
        }

        return child;
    }

}


