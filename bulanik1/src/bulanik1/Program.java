package bulanik1;

import java.util.Scanner;

import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.FIS;

public class Program {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Servis (0-9): ");
        double servis = in.nextDouble();
        System.out.print("Yemek (0-9): ");
        double yemek = in.nextDouble();

        try {
            Restaurant restaurant = new Restaurant(servis, yemek);
            System.out.println(restaurant);
            
            JFuzzyChart.get().chart(restaurant.getModel());
        } catch (Exception e) {
            System.err.println("Model yüklenirken hata oluştu: " + e.getMessage());
            e.printStackTrace();
        } finally {
            in.close();
        }
    }

}