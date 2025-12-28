package sck;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Scanner;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.ElmanNetwork;
import org.neuroph.nnet.learning.BackPropagation;

public class Elman {
	private File egitimDosya;
	private File testDosya;
	
	public Elman() throws URISyntaxException {
		this.egitimDosya = new File(Elman.class.getResource("Egitim.txt").toURI());
		this.testDosya = new File(Elman.class.getResource("Test.txt").toURI());
	}

	public void egit() throws FileNotFoundException {
		// ElmanNetwork(inputNeurons, hiddenNeurons, contextNeurons, outputNeurons)
		// For your original problem: 6 inputs -> 10 hidden -> 11 context -> 1 output
		ElmanNetwork elman = new ElmanNetwork(6, 10, 11, 1);
		
		// Configure BackPropagation
		BackPropagation bp = new BackPropagation();
		bp.setLearningRate(0.1);
		bp.setMaxIterations(5000);
		bp.setMaxError(0.01);
		elman.setLearningRule(bp);
		
		// Load dataset
		DataSet egitimds = veriSeti(egitimDosya);
		
		System.out.println("Training started with " + egitimds.size() + " samples...");
		elman.learn(egitimds);
		System.out.println("Training completed!");
		
		elman.save("model.nnet");
	}

	public double test() throws FileNotFoundException {
		DataSet testds = veriSeti(testDosya);
		
		double toplamHata = 0;
		NeuralNetwork sinirselAg = NeuralNetwork.createFromFile("model.nnet");
		
		System.out.println("Testing " + testds.size() + " samples...");
		
		int count = 0;
		for(DataSetRow satir : testds.getRows()) {
			sinirselAg.setInput(satir.getInput());
			sinirselAg.calculate();
			double[] output = sinirselAg.getOutput();
			double[] expectedOutput = satir.getDesiredOutput();
			
			// Debug: print first few predictions
			if(count < 5) {
				System.out.printf("Sample %d - Expected: %.6f, Got: %.6f%n", 
					count + 1, expectedOutput[0], output[0]);
			}
			
			// Calculate error
			double error = Math.pow(output[0] - expectedOutput[0], 2);
			toplamHata += error;
			count++;
		}
		
		double ortalamaHata = toplamHata / testds.size();
		System.out.printf("Average MSE: %.6f%n", ortalamaHata);
		return ortalamaHata;
	}

	public DataSet veriSeti(File dosya) throws FileNotFoundException {
		Scanner in = new Scanner(dosya);
		// Dataset with 6 inputs and 1 output
		DataSet ds = new DataSet(6, 1);
		
		int satirSayisi = 0;
		while(in.hasNextLine()) {
			String line = in.nextLine().trim();
			if(line.isEmpty()) continue;
			
			String[] values = line.split("\\s+");
			
			// Each row has 7 values: first 6 are inputs, last 1 is output
			if(values.length >= 7) {
				double[] inputs = new double[6];
				for(int i = 0; i < 6; i++) {
					inputs[i] = Double.parseDouble(values[i]);
				}
				
				double[] outputs = new double[1];
				outputs[0] = Double.parseDouble(values[6]);
				
				ds.add(new DataSetRow(inputs, outputs));
				satirSayisi++;
			}
		}
		
		in.close();
		System.out.println("Loaded " + satirSayisi + " rows from " + dosya.getName());
		return ds;
	}
	}