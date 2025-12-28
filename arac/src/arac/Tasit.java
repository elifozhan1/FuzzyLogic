package arac;

import java.io.File;
import java.net.URISyntaxException;
import net.sourceforge.jFuzzyLogic.FIS;

public class Tasit {
	private final FIS fis;
	public Tasit(double mesafe, double sumiktari) throws URISyntaxException {
		File dosya = new File(getClass().getResource("Model.fcl").toURI());
		fis = FIS.load(dosya.getPath(), true);
		fis.setVariable("mesafe", mesafe);
		fis.setVariable("sumiktari", sumiktari);
		fis.evaluate(); // cikti hesaplanmis olur
	}
	public FIS getModel() {
		return fis;
	}

	public long hiz() {
		return Math.round(fis.getVariable("hiz").getValue());
	}
}