import java.io.*;
import java.util.ArrayList;

public class NeuralNetwork {

    static final double TAXA_APRENDIZADO = 0.1;
    static final double TAXA_PESO_INICIAL = 1.0;
    static final int BIAS = 1;

   /* private static void train(RedeNeural net, double[] input, double[] target) {
        NeuralNetwork.RNA_CopiarParaEntrada(net, input);
        NeuralNetwork.RNA_CalcularSaida(net);
        NeuralNetwork.RNA_SalvarRede(net, target);
        NeuralNetwork.RNA_QuantidadePesos(net);
    }*/

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
        Camada CamadaEscondida = new Camada();
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

    @Deprecated(since = "nao estou usando")
    static RedeNeural RNA_CarregarRede(String fileName) {
        int i, j, k;

        try (DataInputStream dis = new DataInputStream(new FileInputStream(fileName))) {
            int QtdEscondida = dis.readInt();
            int QtdNeuroEntrada = dis.readInt();
            int QtdNeuroEscondida = dis.readInt();
            int QtdNeuroSaida = dis.readInt();

            RedeNeural Temp = RNA_CriarRedeNeural(QtdEscondida, QtdNeuroEntrada, QtdNeuroEscondida, QtdNeuroSaida);

            for (i = 0; i < Temp.CamadaEscondida.QuantidadeNeuronios; i++) {
                for (j = 0; j < Temp.CamadaEscondida.Neuronios[i].QuantidadeLigacoes; j++) {
                    Temp.CamadaEscondida.Neuronios[i].Peso[j] = dis.readDouble();
                }
            }
            for (i = 0; i < Temp.CamadaSaida.QuantidadeNeuronios; i++) {
                for (j = 0; j < Temp.CamadaSaida.Neuronios[i].QuantidadeLigacoes; j++) {
                    Temp.CamadaSaida.Neuronios[i].Peso[j] = dis.readDouble();
                }
            }

            return Temp;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    static void RNA_CopiarVetorParaCamadas(RedeNeural Rede, double[] Vetor) {
        int j, i;

        for (i = 0; i < Rede.CamadaEscondida.QuantidadeNeuronios; i++) {
            for (j = 0; j < Rede.CamadaEscondida.Neuronios[i].QuantidadeLigacoes; j++) {
                Rede.CamadaEscondida.Neuronios[i].Peso[j] = Vetor[j];
            }
        }

        for (i = 0; i < Rede.CamadaSaida.QuantidadeNeuronios; i++) {
            for (j = 0; j < Rede.CamadaSaida.Neuronios[i].QuantidadeLigacoes; j++) {
                Rede.CamadaSaida.Neuronios[i].Peso[j] = Vetor[j];
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
        for (int i = 0; i < Rede.CamadaEscondida.QuantidadeNeuronios; i++) {
            Soma = Soma + Rede.CamadaEscondida.Neuronios[i].QuantidadeLigacoes;
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

        for (i = 0; i < Rede.CamadaEscondida.QuantidadeNeuronios - BIAS; i++) {
            Somatorio = 0;
            for (j = 0; j < Rede.CamadaEntrada.QuantidadeNeuronios; j++) {
                Somatorio = Somatorio + Rede.CamadaEntrada.Neuronios[j].Saida * Rede.CamadaEscondida.Neuronios[i].Peso[j];
            }
//            System.out.println(Somatorio);
            Rede.CamadaEscondida.Neuronios[i].Saida = relu(Somatorio);
        }

        for (i = 0; i < Rede.CamadaEscondida.QuantidadeNeuronios - BIAS; i++) {
            Somatorio = 0;
            for (j = 0; j < Rede.CamadaEscondida.QuantidadeNeuronios; j++) {
                Somatorio = Somatorio + Rede.CamadaEscondida.Neuronios[i].Saida * Rede.CamadaEscondida.Neuronios[i].Peso[j];
            }
            Rede.CamadaEscondida.Neuronios[i].Saida = relu(Somatorio);
        }

        for (i = 0; i < Rede.CamadaSaida.QuantidadeNeuronios; i++) {
            Somatorio = 0;
            for (j = 0; j < Rede.CamadaEscondida.QuantidadeNeuronios; j++) {
                int qtdePesos = Rede.CamadaSaida.Neuronios[i].Peso.length;
                for (k = 0; k < qtdePesos; k++) {
                    Somatorio = Somatorio + Rede.CamadaEscondida.Neuronios[j].Saida * Rede.CamadaSaida.Neuronios[i].Peso[k];
                }
            }
//            System.out.println(Somatorio);
            Rede.CamadaSaida.Neuronios[i].Saida = reluDx(Somatorio);
        }
    }

    static void RNA_CriarNeuronio(Neuronio Neuron, int QuantidadeLigacoes) {
        int i;

        Neuron.QuantidadeLigacoes = QuantidadeLigacoes;
        Neuron.Peso = new double[QuantidadeLigacoes];

        for (i = 0; i < QuantidadeLigacoes; i++) {
            Neuron.Peso[i] = (int) (Math.random() * 2000) - 1000;
        }

        Neuron.Erro = 0;
        Neuron.Saida = 1;
    }

    static RedeNeural RNA_CriarRedeNeural(int QuantidadeEscondidas, int QtdNeuroniosEntrada, int QtdNeuroniosEscondida, int QtdNeuroniosSaida) {

        QtdNeuroniosEntrada = QtdNeuroniosEntrada + BIAS;
        QtdNeuroniosEscondida = QtdNeuroniosEscondida + BIAS;

        RedeNeural Rede = new RedeNeural();

        // CRIA ESTRUTURA CAMADA ENTRADA
        Rede.CamadaEntrada.QuantidadeNeuronios = QtdNeuroniosEntrada;
        Rede.CamadaEntrada.Neuronios = new Neuronio[QtdNeuroniosEntrada];

        for (int i = 0; i < QtdNeuroniosEntrada; i++) {
            Rede.CamadaEntrada.Neuronios[i] = new Neuronio();
            RNA_CriarNeuronio(Rede.CamadaEntrada.Neuronios[i], QtdNeuroniosEntrada);
            Rede.CamadaEntrada.Neuronios[i].Saida = 1.0; //já está sendo instanciado  na função acima, posso remover
        }

        // CRIA ESTRUTURA CAMADA ESCONDIDA
        Rede.QuantidadeEscondidas = QuantidadeEscondidas;
        Rede.CamadaEscondida = new Camada();
        Rede.CamadaEscondida.QuantidadeNeuronios = QtdNeuroniosEscondida;
        Rede.CamadaEscondida.Neuronios = new Neuronio[QtdNeuroniosEscondida];

        for (int i = 0; i < QtdNeuroniosEscondida; i++) {
            Rede.CamadaEscondida.Neuronios[i] = new Neuronio();
            RNA_CriarNeuronio(Rede.CamadaEscondida.Neuronios[i], QtdNeuroniosEscondida);
        }

        // CRIA ESTRUTURA CAMADA SAIDA
        Rede.CamadaSaida.QuantidadeNeuronios = QtdNeuroniosSaida;
        Rede.CamadaSaida.Neuronios = new Neuronio[QtdNeuroniosSaida];

        for (int i = 0; i < QtdNeuroniosSaida; i++) {
            Rede.CamadaSaida.Neuronios[i] = new Neuronio();
            RNA_CriarNeuronio(Rede.CamadaSaida.Neuronios[i], QtdNeuroniosSaida);
        }

        return Rede;
    }


    static void RNA_SalvarRede(RedeNeural Temp, String fileName) {
        int i, j, k;

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(fileName))) {
            dos.writeInt(Temp.QuantidadeEscondidas);
            dos.writeInt(Temp.CamadaEntrada.QuantidadeNeuronios);
            dos.writeInt(Temp.CamadaEscondida.QuantidadeNeuronios);
            dos.writeInt(Temp.CamadaSaida.QuantidadeNeuronios);

            for (i = 0; i < Temp.CamadaEscondida.QuantidadeNeuronios; i++) {
                for (j = 0; j < Temp.CamadaEscondida.Neuronios[i].QuantidadeLigacoes; j++) {
                    dos.writeDouble(Temp.CamadaEscondida.Neuronios[i].Peso[j]);
                }
            }

            for (i = 0; i < Temp.CamadaSaida.QuantidadeNeuronios; i++) {
                for (j = 0; j < Temp.CamadaSaida.Neuronios[i].QuantidadeLigacoes; j++) {
                    dos.writeDouble(Temp.CamadaSaida.Neuronios[i].Peso[j]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

/*
    private static void calculateError(RedeNeural net, double[] target) {
        int i, j, k;

        for (i = 0; i < net.CamadaSaida.QuantidadeNeuronios; i++) {
            net.CamadaSaida.Neuronios[i].Erro = (target[i] - net.CamadaSaida.Neuronios[i].Saida) * reluDx(net.CamadaSaida.Neuronios[i].Saida);
        }

        for (k = net.CamadaEscondida.length - 1; k >= 0; k--) {
            for (i = 0; i < net.CamadaEscondida[k].numNeurons - BIAS; i++) {
                sum = 0;
                if (k == net.CamadaEscondida.length - 1) {
                    for (j = 0; j < net.CamadaSaida.numNeurons; j++) {
                        sum += net.CamadaSaida.Neuronios[j].weights[i] * net.CamadaSaida.Neuronios[j].error;
                    }
                } else {
                    for (j = 0; j < net.CamadaEscondida[k + 1].QuantidadeNeuronios - BIAS; j++) {
                        sum += net.CamadaEscondida[k + 1].Neuronios[j].weights[i] * net.CamadaEscondida[k + 1].Neuronios[j].error;
                    }
                }
                net.CamadaEscondida[k].Neuronios[i].Erro = reluDx(net.CamadaEscondida[k].Neuronios[i].Saida) * sum;
            }
        }
    }*/

}


