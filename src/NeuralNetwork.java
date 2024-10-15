import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

public class NeuralNetwork {

    private static final double LEARNING_RATE = 0.1;
    private static final int BIAS = 1;

    // Classes internas para estrutura da rede neural
    public static class Neuron {
        double[] weights;
        double error;
        double output;
        int connections;

        public Neuron(int connections) {
            this.connections = connections;
            this.weights = new double[connections];
            initializeWeights();
        }

        private void initializeWeights() {
            for (int i = 0; i < connections; i++) {
                weights[i] = Utility.getRandomValue();
            }
            error = 0;
            output = 1;
        }
    }

    public static class Layer {
        Neuron[] neurons;
        int neuronCount;

        public Layer(int neuronCount, int connections) {
            this.neuronCount = neuronCount;
            neurons = new Neuron[neuronCount];
            for (int i = 0; i < neuronCount; i++) {
                neurons[i] = new Neuron(connections);
            }
        }
    }

    // Estrutura da rede neural
    public Layer inputLayer;
    public Layer[] hiddenLayers;
    public Layer outputLayer;
    public int hiddenLayerCount;

    // Métodos para criação e manipulação da rede
    public static NeuralNetwork createNetwork(int inputNeurons, int hiddenNeurons, int outputNeurons, int hiddenLayers) {
        NeuralNetwork network = new NeuralNetwork();
        network.inputLayer = new Layer(inputNeurons + BIAS, 0);
        network.hiddenLayerCount = hiddenLayers;
        network.hiddenLayers = new Layer[hiddenLayers];

        // Criação das camadas escondidas
        for (int i = 0; i < hiddenLayers; i++) {
            int connections = (i == 0) ? inputNeurons + BIAS : hiddenNeurons + BIAS;
            network.hiddenLayers[i] = new Layer(hiddenNeurons + BIAS, connections);
        }

        // Criação da camada de saída
        network.outputLayer = new Layer(outputNeurons, hiddenNeurons + BIAS);
        return network;
    }

    public static int getWeightCount(NeuralNetwork network) {
        int count = 0;
        for (Layer layer : network.hiddenLayers) {
            for (Neuron neuron : layer.neurons) {
                count += neuron.connections;
            }
        }
        for (Neuron neuron : network.outputLayer.neurons) {
            count += neuron.connections;
        }
        return count;
    }

    public static void saveNetwork(NeuralNetwork network, int generation, String startTime) {
        // Implementação da lógica para salvar a rede neural em arquivo
    }

    public static NeuralNetwork crossoverAndMutate(NeuralNetwork parent1, NeuralNetwork parent2, double mutationRate) {
        NeuralNetwork child = createNetwork(
                parent1.inputLayer.neuronCount - BIAS,
                parent1.hiddenLayers[0].neuronCount - BIAS,
                parent1.outputLayer.neuronCount,
                parent1.hiddenLayerCount
        );

        // Crossover dos pesos
        for (int i = 0; i < parent1.hiddenLayers.length; i++) {
            for (int j = 0; j < parent1.hiddenLayers[i].neurons.length; j++) {
                for (int k = 0; k < parent1.hiddenLayers[i].neurons[j].weights.length; k++) {
                    // Seleciona o peso de um dos pais
                    if (Math.random() < 0.5) {
                        child.hiddenLayers[i].neurons[j].weights[k] = parent1.hiddenLayers[i].neurons[j].weights[k];
                    } else {
                        child.hiddenLayers[i].neurons[j].weights[k] = parent2.hiddenLayers[i].neurons[j].weights[k];
                    }
                    // Aplica mutação
                    if (Math.random() < mutationRate) {
                        child.hiddenLayers[i].neurons[j].weights[k] += Utility.getRandomValue() * 0.1;
                    }
                }
            }
        }

        // Repetir o processo para a camada de saída
        for (int j = 0; j < parent1.outputLayer.neurons.length; j++) {
            for (int k = 0; k < parent1.outputLayer.neurons[j].weights.length; k++) {
                if (Math.random() < 0.5) {
                    child.outputLayer.neurons[j].weights[k] = parent1.outputLayer.neurons[j].weights[k];
                } else {
                    child.outputLayer.neurons[j].weights[k] = parent2.outputLayer.neurons[j].weights[k];
                }
                if (Math.random() < mutationRate) {
                    child.outputLayer.neurons[j].weights[k] += Utility.getRandomValue() * 0.1;
                }
            }
        }

        return child;
    }

    // Métodos para cálculos internos da rede
    private static double relu(double x) {
        return Math.max(0, x);
    }

    private static double reluDerivative(double x) {
        return x > 0 ? 1 : 0;
    }


    public void feedInputs(double[] inputs) {
        for (int i = 0; i < inputs.length; i++) {
            inputLayer.neurons[i].output = inputs[i];
        }
    }

    public void calculateOutput() {
        // Cálculo para a primeira camada escondida
        for (int i = 0; i < hiddenLayers[0].neuronCount; i++) {
            double sum = 0;
            for (int j = 0; j < inputLayer.neuronCount; j++) {
                sum += inputLayer.neurons[j].output * hiddenLayers[0].neurons[i].weights[j];
            }
            hiddenLayers[0].neurons[i].output = relu(sum);
        }

        // Cálculo para camadas escondidas adicionais, se houver
        for (int k = 1; k < hiddenLayerCount; k++) {
            for (int i = 0; i < hiddenLayers[k].neuronCount; i++) {
                double sum = 0;
                for (int j = 0; j < hiddenLayers[k - 1].neuronCount; j++) {
                    sum += hiddenLayers[k - 1].neurons[j].output * hiddenLayers[k].neurons[i].weights[j];
                }
                hiddenLayers[k].neurons[i].output = relu(sum);
            }
        }

        // Cálculo para a camada de saída
        for (int i = 0; i < outputLayer.neuronCount; i++) {
            double sum = 0;
            for (int j = 0; j < hiddenLayers[hiddenLayerCount - 1].neuronCount; j++) {
                sum += hiddenLayers[hiddenLayerCount - 1].neurons[j].output * outputLayer.neurons[i].weights[j];
            }
            outputLayer.neurons[i].output = relu(sum);
        }
    }

    public double[] extractOutputs() {
        double[] outputs = new double[outputLayer.neuronCount];
        for (int i = 0; i < outputLayer.neuronCount; i++) {
            outputs[i] = outputLayer.neurons[i].output;
        }
        return outputs;
    }

}
