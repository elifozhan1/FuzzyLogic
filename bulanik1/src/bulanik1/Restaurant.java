package bulanik1;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class Restaurant {
    private FIS fis;
    private double servis;
    private double yemek;

    public Restaurant(double servis, double yemek) {
        this.servis = servis;
        this.yemek = yemek;

        try {
            // Model.fcl, bulanik1 paketinin içinde (aynı klasörde) olmalı
            URL modelUrl = getClass().getResource("Model.fcl");
            if (modelUrl == null) {
                throw new IllegalStateException("Model.fcl bulunamadı. Dosyayı bulanik1 paketinin içine koyun.");
            }
            File modelDosyasi = new File(modelUrl.toURI());

            // FIS yükle
            fis = FIS.load(modelDosyasi.getPath(), true);
            if (fis == null) {
                throw new IllegalStateException("FIS nesnesi oluşturulamadı (dosya hatalı olabilir).");
            }

            // Girdi değişkenlerini ayarla
            fis.getVariable("servis").setValue(this.servis);
            fis.getVariable("yemek").setValue(this.yemek);

            // Değerlendir
            fis.evaluate();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Model dosyası URI hatası: " + e.getMessage(), e);
        }
    }

    // Program.java JFuzzyChart için FIS nesnesini istiyor
    public FIS getModel() {
        return fis;
    }

    @Override
    public String toString() {
        Variable tur = fis.getVariable("tur");
        double turDegeri = tur.getValue();
        return "Servis: " + servis + "\n" +
               "Yemek: " + yemek + "\n" +
               "Hesaplanan Tür: " + turDegeri;
    }
}
