package cmr;

import java.io.File;
import java.net.URISyntaxException;
import net.sourceforge.jFuzzyLogic.FIS;

public class CamasirMakinesi {
	private final FIS fis;
    private final double kirlilikOrani, camasirTuru;
    
    public CamasirMakinesi(double kirlilikOrani,double camasirTuru)throws URISyntaxException{
    	this.kirlilikOrani = kirlilikOrani;
        this.camasirTuru=camasirTuru;
        
        File file = new File(getClass().getResource("Model.fcl").toURI());
        fis = FIS.load(file.getPath(),true);
        fis.setVariable("kirlilikorani", kirlilikOrani);
        fis.setVariable("camasirturu", camasirTuru);
        fis.evaluate();
    }
    public FIS getModel() {
    	return fis;
    }
    @Override
    public String toString() {
    	return "Çalışma süresi: "+Math.round(fis.getVariable("yikamasuresi").getValue())+"dakika";
    }

}