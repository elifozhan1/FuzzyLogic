package nn;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.Perceptron;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;

public class Ysa {
	
	public double[] hatalar;
    private final int maxEpoch = 1000;
    
	private static final File dosya = new File(Ysa.class.getResource("Data.txt").getPath());
    
    private DataSet egitimVeriSeti = null; 
    public Ysa() throws FileNotFoundException {
        this.egitimVeriSeti = veriSeti(); 
    }
	
	public void egit() {
		NeuralNetwork sinirselAg = new Perceptron(2,1);
		sinirselAg.learn(this.egitimVeriSeti); 
		sinirselAg.save("model.nnet");
		System.out.println("Eğitim tamamlandı. (Perceptron) -> model.nnet kaydedildi.");
	}
	
	public void manuelegit(int maxEpoch) throws FileNotFoundException {
		MultiLayerPerceptron sinirselAg =
				new MultiLayerPerceptron(TransferFunctionType.SIGMOID,2,5,3,1);
		BackPropagation bp = new BackPropagation();
		bp.setLearningRate(0.2);
		bp.setMaxError(0.001);
		sinirselAg.setLearningRule(bp);
		
		this.hatalar = new double[maxEpoch];
		
		for(int i=0;i<maxEpoch;i++) {
			sinirselAg.getLearningRule().doOneLearningIteration(this.egitimVeriSeti); 
			
			if(i==0) hatalar[0] = sinirselAg.getLearningRule().getPreviousEpochError();
			else hatalar[i] = sinirselAg.getLearningRule().getPreviousEpochError();
		}
        sinirselAg.save("manual_model.nnet");
        System.out.println("Manuel Eğitim tamamlandı. (MLP) -> manual_model.nnet kaydedildi.");
	}
	
	public DataSet veriSeti() throws FileNotFoundException {
		Scanner in = new Scanner(dosya);
        in.useDelimiter("[\\s,]+"); 
		DataSet ds = new DataSet(2,1);
		
		while(in.hasNextDouble()) {
			
			DataSetRow satir = new DataSetRow(
					new double [] {in.nextDouble(), in.nextDouble()},
					new double[] {in.nextDouble()}
					);
			ds.add(satir);
		}
		in.close();
		return ds;
	}
	
	public double test(double x, double y) {
        
		NeuralNetwork sinirselAg = NeuralNetwork.createFromFile("model.nnet"); 
        
        if (sinirselAg == null) {
            System.out.println("HATA: 'model.nnet' bulunamadı. Lütfen önce Ağı Eğit (Seçenek 1) seçeneğini çalıştırın.");
            return -1.0;
        }
        
		sinirselAg.setInput(x,y);
		sinirselAg.calculate();
		return sinirselAg.getOutput()[0];
	}
}