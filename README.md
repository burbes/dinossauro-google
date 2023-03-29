# [Google Dinossaur A.I. ](chrome://dino)


================================================================================================

* Speed control (CLOCK between 1 and 3)

  * Scrow Up - Increase Clock (Velocity)
  * Scrow Down - Decrease Clock (Velocity)


================================================================================================

# About the project

The idea is to use an "artificial selection" to adjust the weights using a generic algorithm based on reward for the best dinosaurs. At each iteration, up to 1000 dinosaurs are rotated and the 2 best are saved. With the 2 best, we created the rest of the dinosaurs with variations of the 2 best of all generations in order to find the best combination and thus obtain the best behavior for the character.

To decide the actions (Run, Jump, Down), the Multilayer Perceptron (MLP) was used, which is an artificial neural network architecture fed forward (feedforward) that consists of several layers of connected nodes.

- Input Layer with 6 sensors + 1 Bias, totaling 7 Neurons
- Hidden Layer with 6 neurons + 1 Bias, totaling 7 Neurons
- Output Layer with 3 neurons (Run, Jump, Down)
- The activation function used in all neurons was ReLU.
- The population size I used ranged from 100 ~ 1000 individuals.
- Learning time varied between 15 ~ 45 minutes per Game mode.

================================================================================================

Neural Network Winner

**Best Dinossaur**
```agsl
GERAÇÃO: 42
Camada escondida 1:
	Neuronio 1:
		Peso 1: 30,7			Peso 2: 983,8			Peso 3: 845			Peso 4: -92,6			Peso 5: 559,1			Peso 6: -345,4			Peso 7: -343	
	Neuronio 2:
		Peso 1: -295,2			Peso 2: -600,9			Peso 3: -176,5			Peso 4: 386,8			Peso 5: 642,4			Peso 6: -425,6			Peso 7: 428,8	
	Neuronio 3:
		Peso 1: 512,8			Peso 2: 736,7			Peso 3: -737			Peso 4: 778,9			Peso 5: -207,5			Peso 6: 659,3			Peso 7: 670,4	
	Neuronio 4:
		Peso 1: -871,2			Peso 2: 527,2			Peso 3: -637,8			Peso 4: 737,3			Peso 5: 889,9			Peso 6: 980,1			Peso 7: 853,9	
	Neuronio 5:
		Peso 1: 191,8			Peso 2: 948,8			Peso 3: -719,1			Peso 4: 196,3			Peso 5: 351,2			Peso 6: 896,6			Peso 7: 592,8	
	Neuronio 6:
		Peso 1: -645,6			Peso 2: -830,4			Peso 3: 341,6			Peso 4: 355,9			Peso 5: 62,7			Peso 6: 579,3			Peso 7: -802,3	
	Neuronio 7:
		Peso 1: -927,5			Peso 2: 812,4			Peso 3: 478,2			Peso 4: 169,6			Peso 5: 680			Peso 6: 779			Peso 7: 959	
Camada de saida:
	Neuronio 1:
		Peso 1: 707,8			Peso 2: 460,4			Peso 3: 762,2			Peso 4: -132,4			Peso 5: -294,1			Peso 6: 308,2			Peso 7: -808,3	
	Neuronio 2:
		Peso 1: -104,8			Peso 2: 731,6			Peso 3: 46,4			Peso 4: -613,1			Peso 5: 853,4			Peso 6: -145,5			Peso 7: 138,6	

=====================
```

Sorry for non-Portuguese speakers, most of the prints are in Portuguese, but methods and variables are in English ':D

Need help? https://www.linkedin.com/in/naelsonm/