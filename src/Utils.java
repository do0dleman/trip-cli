public class Utils {
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
