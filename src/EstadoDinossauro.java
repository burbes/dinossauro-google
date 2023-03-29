public class EstadoDinossauro {

    public EstadoDinossauro() {

    }

    public MainCharacterStateEnum controlarEstadoDinossauros(MainCharacter dinossauro, Enemy enemy) {

        MainCharacterStateEnum estadoDinoEnum = MainCharacterStateEnum.NORMAL_RUN;
        double[] saida = new double[10];
        double[] entrada = new double[10];

        if (dinossauro.isAlive()) {
            entrada[0] = distanciaProximoObstaculo(dinossauro.getX(), enemy.getBound().getX());
            entrada[1] = enemy.getBound().getWidth(); //larguraProximoObstaculo
            entrada[2] = enemy.getBound().getY(); //alturaProximoObstaculo
            entrada[3] = enemy.getBound().getHeight(); //comprimentoProximoObstaculo
            entrada[4] = Math.abs(GameScreen.VELOCIDADE);
            entrada[5] = dinossauro.getBound().getHeight();//comprimentoDinossauo

            NeuralNetwork.RNA_CopiarParaEntrada(dinossauro.cerebro, entrada);/// Enviando informações para a rede neural
            NeuralNetwork.RNA_CalcularSaida(dinossauro.cerebro); /// Calculando a decisão da rede
            NeuralNetwork.RNA_CopiarDaSaida(dinossauro.cerebro, saida);/// Extraindo a decisão para vetor ''saida''

//            System.out.println("dinossauro: " + dinossauro + " saida[0]: " + saida[0] + " saida[1]: " + saida[1]);

            if (saida[0] == 0.0 && saida[1] == 0.0) {
                estadoDinoEnum = MainCharacterStateEnum.NORMAL_RUN;
            } else {
                if (saida[0] != 0.0 && saida[0] > saida[1]) {
                    estadoDinoEnum = MainCharacterStateEnum.JUMPING;
                } else if (saida[1] != 0.0) {
                    estadoDinoEnum = MainCharacterStateEnum.DOWN_RUN;
                }
            }
            //System.out.println(" estadoDinoEnum: " + estadoDinoEnum.name());

        }

        return estadoDinoEnum;
    }

    double distanciaProximoObstaculo(double x, double enemyX) {
        return enemyX - x;
    }

}
