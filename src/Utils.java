import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Utils {
    public void generateDbFile(String filename) {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(filename));
            out.print("101;Daugavpils;07/07/2024;2;100.00;TRAIN\n" +
                    "102;Warsaw;15/05/2025;7;300.00;BUS\n" +
                    "103;Berlin;12/03/2025;3;500.50;PLANE\n" +
                    "104;Stockholm;10/06/2026;2;400.00;BOAT\n" +
                    "105;New York;16/12/2025;7;1000.00;PLANE");

            out.close();
            System.gc();
        } catch (Exception ex) {
            System.out.println("Error during creating default db.csv: " + ex.getMessage());
            return;
        }
        System.out.println("Created default db.csv");
    }

    public boolean isDbFileExists(String filename) {
        File f = new File(filename);
        if (!f.exists()) {
            System.out.println("The file db.csv does not exist");
            return false;
        }

        return true;
    }

    public String formatTripFields(String trip) {
        String[] tripSplit = trip.split(";");

        if(!tripSplit[1].isBlank()) {
            tripSplit[1] = capitalize(tripSplit[1].toLowerCase());
        }

        try {
            float price = Float.parseFloat(tripSplit[4]);
            tripSplit[4] = String.format("%.2f", price);
        } catch (Exception _) {

        }
        if(!tripSplit[5].isBlank()) {
            tripSplit[5] = tripSplit[5].toUpperCase();
        }

        return String.join(";", tripSplit);
    }

    public String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
