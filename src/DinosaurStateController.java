public class DinosaurStateController {

    public MainCharacterState controlDinosaurState(MainCharacter dinosaur, Enemy enemy) {
        double[] inputs = new double[6];
        double[] outputs;

        if (dinosaur.isAlive()) {
            inputs[0] = getDistanceToNextObstacle(dinosaur.getX(), enemy.getBoundingBox().getX()) / GameScreen.SCREEN_WIDTH;
            inputs[1] = enemy.getBoundingBox().getWidth() / GameScreen.SCREEN_WIDTH;
            inputs[2] = enemy.getBoundingBox().getY() / GameScreen.SCREEN_HEIGHT;
            inputs[3] = enemy.getBoundingBox().getHeight() / GameScreen.SCREEN_HEIGHT;
            inputs[4] = Math.abs(GameScreen.globalSpeed) / 100; // Assumindo que a velocidade máxima seja 100
            inputs[5] = dinosaur.getBoundingBox().getHeight() / GameScreen.SCREEN_HEIGHT;

            // Alimenta a rede neural com as entradas
            dinosaur.getBrain().feedInputs(inputs);
            dinosaur.getBrain().calculateOutput();
            outputs = dinosaur.getBrain().extractOutputs();

            if (outputs[0] > outputs[1]) {
                return MainCharacterState.JUMPING;
            } else if (outputs[1] > outputs[0]) {
                return MainCharacterState.DUCKING;
            } else {
                return MainCharacterState.RUNNING;
            }
        }
        return MainCharacterState.RUNNING;
    }

    private double getDistanceToNextObstacle(double dinosaurX, double enemyX) {
        return enemyX - dinosaurX;
    }
}
