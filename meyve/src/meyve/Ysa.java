package meyve;

import java.io.File;
import java.net.URISyntaxException;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.MultiLayerPerceptron; 
import org.neuroph.util.TransferFunctionType;

public class Ysa {
	private File dosya;
	NeuralNetwork<BackPropagation> sinirselAg;
	DataSet data;
	BackPropagation bp;
	
	public Ysa(int epoch, double lr) throws URISyntaxException {
		init(epoch, lr);
	}
	
	void init(int epoch, double lr) throws URISyntaxException {
		dosya = new File(Ysa.class.getResource("Data.txt").toURI());
		data = DataSet.createFromFile(dosya.getPath(),8,4, " ");
		sinirselAg = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,8,20,8,4);
		//girdi:8, ara katman:10, çıktı:4 nöron sayıları gibi
		bp = new BackPropagation();
		bp.setLearningRate(lr);
		bp.setMaxIterations(epoch);
		sinirselAg.setLearningRule(bp);
	}
	
	//k-fold cross validation
	public void trainAndTest(int k) {
		DataSet[] folds = createFolds(data,k);
		double toplamEgitimHata=0;
		double toplamTestHata= 0;
		for(int i=0; i<k; i++) {
			DataSet[] trainTest = selectTrainAndTest(folds, i);
			sinirselAg.randomizeWeights(); //ağırlıkları sıfırlar
			sinirselAg.learn(trainTest[1]);
			toplamEgitimHata += bp.getTotalNetworkError();
			toplamTestHata += test(sinirselAg, trainTest[0]);
			//fine tuning: eğitilmiş ağın alt kategoride uzmanlaşması
		}
		System.out.println("K-Fold Ortalama Eğitim"+toplamEgitimHata/k);
		System.out.println("K-Fold Ortalama Test"+toplamTestHata/k);
	}
	
	public double test(NeuralNetwork<BackPropagation> sinirselAg, DataSet testds) {
		double toplamHata = 0;
		for(DataSetRow row : testds.getRows()) {
			sinirselAg.setInput(row.getInput());
			sinirselAg.calculate();
			toplamHata += mse(row.getDesiredOutput(), sinirselAg.getOutput());
			//döngüde ortalama hata bulunuyor
		}
		return toplamHata/testds.size();
	}
	
	//MSE tek satırdaki hatayı buluyor
	private double mse(double[] beklenen, double[] uretilen) {
		double hata=0;
		for(int i= 0; i <beklenen.length; i++) {
			hata += Math.pow(beklenen[i]-uretilen[i], 2);
		}
		return hata/beklenen.length;
	}
	
	private DataSet[] selectTrainAndTest(DataSet[] folds, int testIndex) {
		DataSet[] trainTest = new DataSet[2];
		trainTest[0] = folds[testIndex];
		trainTest[1] = new DataSet(8,4);
		for(int i=0;i<folds.length;i++) {
			if(i == testIndex) continue;
			trainTest[1].addAll(folds[i]);
		}
		return trainTest;
	}
	
	DataSet[] createFolds(DataSet dataset, int k) {
		DataSet[] folds = new DataSet[k];
		int rows = dataset.size()/k; //tam sayı bölmesi
		for(int i = 0, j = -1; i <dataset.size(); i++) {
			if(i%rows == 0) {
				j++;
				folds[j] = new DataSet(dataset.getInputSize(), dataset.getOutputSize());
			}

			folds[j].add(dataset.getRowAt(i));
		}
		return folds;
	}

}