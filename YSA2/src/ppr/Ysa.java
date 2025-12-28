package ppr;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.Perceptron;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;

public class Ysa {
    private static final File egitimDosya =
            new File(Ysa.class.getResource("Egitim.txt").getPath());
    private static final File testDosya =
            new File(Ysa.class.getResource("Test.txt").getPath());
    
    private double[] maksimumlar = new double[6]; 
    private double[] minimumlar = new double[6]; 
    
    private DataSet egitimVeriSeti;
    private DataSet testVeriSeti;
    private int araKatmanNoronSayisi;
    MomentumBackpropagation mbp;
    
    Ysa(int araKatmanNoronSayisi, double momentum, double ogrkat, double error, int epoch) throws FileNotFoundException {
        
        for(int i=0; i<6; i++) {
            maksimumlar[i] = Double.MIN_VALUE;
            minimumlar[i] = Double.MAX_VALUE;
        }
        
        maxminAyarla(egitimDosya);
        maxminAyarla(testDosya);
        
        egitimVeriSeti = veriSetiOlustur(egitimDosya);
        testVeriSeti = veriSetiOlustur(testDosya);
        
        mbp = new MomentumBackpropagation();
        mbp.setMomentum(momentum);
        mbp.setLearningRate(ogrkat);
        mbp.setMaxError(error);
        mbp.setMaxIterations(epoch);
        this.araKatmanNoronSayisi = araKatmanNoronSayisi;
    }
    
    private double min_max(double d, double max, double min) {
        if (max == min) return 0.5;
        return (d - min) / (max - min);
    }
    
    public double egitimHata() {
        return mbp.getTotalNetworkError();
    }
    
    public void egit() {
        MultiLayerPerceptron sinirselAg = 
            new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 8, araKatmanNoronSayisi, 3);
        
        sinirselAg.setLearningRule(mbp);
        sinirselAg.learn(egitimVeriSeti);
        sinirselAg.save("model.nnet"); 
        System.out.println("Eğitim Tamamlandı.");
    }
    
    private double mse(double[] beklenen, double[] cikti) {
        double toplamHata=0;
        for(int i=0; i<3; i++) toplamHata += Math.pow(beklenen[i] - cikti[i], 2);
        return toplamHata / 3;
    }
    
    public double test() { 
        NeuralNetwork sinirselAg = NeuralNetwork.createFromFile("model.nnet");
        if (sinirselAg == null) {
             System.out.println("HATA: Model dosyası bulunamadı.");
             return -1.0;
        }
        
        double toplamHata = 0;
        for(DataSetRow satir : testVeriSeti) {
            sinirselAg.setInput(satir.getInput());
            sinirselAg.calculate();
            
            toplamHata += mse(satir.getDesiredOutput(), sinirselAg.getOutput());
        }
        return toplamHata / testVeriSeti.size();
    }
    
    // ham girdiler ve ülke
    public String test(double[] hamgirdiler, String ulke) {
        double[] inputs = new double[8];
        
        for(int i=0; i<6; i++) {
            inputs[i] = min_max(hamgirdiler[i], maksimumlar[i], minimumlar[i]);
        }
        
        switch(ulke.toLowerCase()) { 
            case "america":
                inputs[6] = 0;
                inputs[7] = 0;
                break;
            case "asia":
                inputs[6] = 0;
                inputs[7] = 1;
                break;
            case "europe":
            default:
                inputs[6] = 1;
                inputs[7] = 0;
                break;
        }
        
        NeuralNetwork sinirselAg = NeuralNetwork.createFromFile("model.nnet");
        if (sinirselAg == null) return "HATA: Model dosyası bulunamadı!";
        
        sinirselAg.setInput(inputs);
        sinirselAg.calculate();
        return getSonuc(sinirselAg.getOutput());
    }
    
    private String getSonuc(double[] outputs) {
        int index = 0;
        double max = outputs[0];
        
        if(outputs[1] > max) { 
            max = outputs[1]; 
            index = 1;
        }
        if(outputs[2] > max) { 
            max = outputs[2]; 
            index = 2;
        }
        
        switch(index) {
            case 0:
                return "Kötü"; 
            case 1:
                return "Normal"; 
            case 2:
                return "İyi"; 
            default:
                return "Bilinmiyor";
        }
    }

    private void maxminAyarla(File dosya) throws FileNotFoundException {
        Scanner in = new Scanner(dosya).useLocale(Locale.US);
        in.useDelimiter("\\s+"); 
        //dosyanızda ondalık ayırıcı (,) ise (.), locale ayarını kullanın

        while(in.hasNextDouble()) {
            
            for(int i=0; i<6; i++) {
                double d = in.nextDouble();
                if(d > maksimumlar[i]) maksimumlar[i] = d;
                if(d < minimumlar[i]) minimumlar[i] = d;
            }
            
            for(int i=0; i<5; i++) {
                in.nextDouble();
            }
        }
        in.close();
    }
    
    private DataSet veriSetiOlustur(File dosya) throws FileNotFoundException {
    	
    	Scanner in = new Scanner(dosya).useLocale(Locale.US); 
    	in.useDelimiter("\\s+");
    	DataSet ds = new DataSet(8, 3); 
    	
    	while (in.hasNextDouble()) {
    		
    		double[] inputs = new double[8];
    		for(int i=0; i<6; i++) { 
    			double d = in.nextDouble();
    			inputs[i] = min_max(d, maksimumlar[i], minimumlar[i]);
    		}
         
    		// ülke girdileri
    		for(int i=6; i<8; i++) {
    			inputs[i] = in.nextDouble(); 
    		}
         
    		// çıktılar
    		double[] outputs = new double[3];
    		for(int i=0; i<3; i++) {
    			outputs[i] = in.nextDouble();
    		}
         
    		DataSetRow row = new DataSetRow(inputs, outputs);
    		ds.add(row); 
    	}
     
    	if (ds.size() == 0) {
         	System.err.println("HATA: Veri Seti boş oluşturuldu. Dosya boş olabilir veya format hatası devam ediyor.");
    	}
     
    	in.close();
    	return ds;
    }
}