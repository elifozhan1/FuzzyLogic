package meyve;

import java.net.URISyntaxException;

public class Program {
	
	public static void main (String[] args) throws URISyntaxException {
		Ysa ysa = new Ysa(2000, 0.2);
		ysa.trainAndTest(3);
		
		//not: data.txt'de ; yerine boşluk
		//, yerine . kullanılmalı
	}

}