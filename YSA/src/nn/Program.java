package nn;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Program {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		Ysa ysa = new Ysa();
		Scanner in = new Scanner(System.in);
		int secenek=0;
		
		do {
			System.out.println("\n--- Yapay Sinir Ağı Menüsü ---");
			System.out.println("1. Ağı Eğit (Perceptron)");
			System.out.println("2. Ağı Manuel Eğit (MLP) ve Hataları Göster");
			System.out.println("3. Ağı Test Et");
			System.out.println("4. Çıkış");
			System.out.print("Seçeneğiniz: ");
            
            try {
                secenek = in.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Hata: Lütfen sadece sayı giriniz.");
                in.next(); 
                continue;
            }
            
			switch(secenek) {
			case 1:
				ysa.egit();
				break;
			case 2:
                try {
                    ysa.manuelegit(1000);
                    double[] hatalar = ysa.hatalar;
                    System.out.println("\n--- Eğitim Hataları ---");
                    for(double hata : hatalar) {
                        System.out.printf("%.6f\n", hata);
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("HATA: Eğitim verisi (Data.txt) bulunamadı.");
                }
				break;
			case 3:
				double x,y;
				System.out.print("x:");
				x=in.nextDouble();
				System.out.print("y:");
				y=in.nextDouble();
				double z = ysa.test(x, y);
				System.out.println("z:"+z);
				break;
			case 4:
                System.out.println("Programdan çıkılıyor...");
				break;
            default:
                System.out.println("Geçersiz seçenek. Lütfen 1-4 arasında bir sayı giriniz.");
                break;
			}
		} while(secenek != 4);
		
		in.close();
	}
}