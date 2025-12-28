package ppr;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Program {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		Scanner in = new Scanner(System.in);
		int araKatmanNoronSayisi;
		double momentum, ogrenmeKatsayisi, error;
		int epoch, sec = 0;
		Ysa ysa = null;
        
		do {
            System.out.println("\n--- Ana Menü ---");
			System.out.println("1. Eğitim ve Test");
			System.out.println("2. Tekli Test");
			System.out.println("3. Çıkış");
            System.out.print("Seçiminiz: ");
            
            try {
                sec = in.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Hata: Lütfen geçerli bir sayı giriniz.");
                in.next(); 
                continue;
            }

			switch(sec) {
			case 1:
				System.out.print("Ara Katman Nöron Sayısı: ");
				araKatmanNoronSayisi = in.nextInt();
				System.out.print("Momentum: ");
				momentum = in.nextDouble();
				System.out.print("Öğrenme Katsayısı: ");
				ogrenmeKatsayisi = in.nextDouble();
				System.out.print("Min Hata: ");
				error = in.nextDouble();
				System.out.print("Epoch Sayısı: ");
				epoch = in.nextInt();
                
                try {
                    ysa = new Ysa(araKatmanNoronSayisi, momentum, ogrenmeKatsayisi, error, epoch);
                    ysa.egit();
                    
                    System.out.printf("Eğitimdeki Son Hata: %.6f\n", ysa.egitimHata());
                    System.out.printf("Testteki Ortalama Hata: %.6f\n", ysa.test());
                } catch (FileNotFoundException e) {
                    System.err.println("HATA: 'Egitim.txt' veya 'Test.txt' dosyası bulunamadı.");
                }
				break;
			
			case 2:
				if(ysa == null) {
					System.out.println("Önce eğitim yapılmalı (1. Seçenek).");
					in.nextLine();
					break;
				}
				double[] hamDegerler = new double[6];
				
				System.out.print("1. Silindir (Cins_Sayısı): ");
				hamDegerler[0] = in.nextDouble();
				System.out.print("2. Motor Hacmi: ");
				hamDegerler[1] = in.nextDouble();
				System.out.print("3. Beygir Gücü: ");
				hamDegerler[2] = in.nextDouble();
				System.out.print("4. Ağırlık: ");
				hamDegerler[3] = in.nextDouble();
				System.out.print("5. Hızlanma: ");
				hamDegerler[4] = in.nextDouble();
				System.out.print("6. Yıl: ");
				hamDegerler[5] = in.nextDouble();
				
				System.out.print("7. Ülke (America/Asia/Europe): ");
				String ulke = in.next();
				
				String sonuc = ysa.test(hamDegerler, ulke);
				System.out.println("Tahmin Edilen Durum: "+sonuc);
				break;
                
            case 3:
                System.out.println("Çıkış yapılıyor...");
                break;
                
            default:
                System.out.println("Geçersiz seçenek. Lütfen 1, 2 veya 3 giriniz.");
			}
		} while(sec != 3);
        
        in.close();
	}
}