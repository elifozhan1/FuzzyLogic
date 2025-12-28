package cmr;

import java.net.URISyntaxException;
import java.util.Scanner;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;

public class Program {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
        System.out.print("Kirlilik Oranı (0-100):");
        double kirlilikOrani = in.nextDouble();
        System.out.print("Çamaşır Türü (0-100):");
        double camasirTuru = in.nextDouble();
        try {
	        CamasirMakinesi camasirMakinesi = new CamasirMakinesi(kirlilikOrani, camasirTuru);
	        System.out.println(camasirMakinesi);	        
	        JFuzzyChart.get().chart(camasirMakinesi.getModel());
        }
        catch (URISyntaxException ex) {
        	System.out.println(ex.getMessage());
		}
	}

}