package sck;

public class Test {

	public static void main(String[] args) throws Exception {
		Elman elman = new Elman();
		elman.egit();
		
		double hata = elman.test();
		System.out.println("Test Hata Oranı: " + hata);
	}

}
