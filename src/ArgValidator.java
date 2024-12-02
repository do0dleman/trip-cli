public class ArgValidator {
    public boolean isEditArgValid(String arg) {
        if(arg.isBlank()) {
            System.out.println("This command requires 1 argument");
            return false;
        }

        String[] tripSplit = arg.split(";");
        if(tripSplit.length != 6) {
            System.out.println("wrong field count");
            return false;
        }

        String id = tripSplit[0];
        try {
            int num = Integer.parseInt(id);

            if(num < 100 || num > 999) {
                System.out.println("Id has to be a 3 digit number");
                return false;
            }
        } catch (Exception ex) {
            System.out.println("Error, id has to be a number");
            return false;
        }

        if (!tripSplit[2].isBlank()) {
            String[] DateSplit = tripSplit[2].split("/");

            try {
                int day = Integer.parseInt(DateSplit[0]);
                int month = Integer.parseInt(DateSplit[1]);
                int year = Integer.parseInt(DateSplit[2]);

                if(day <= 0 || day > 31) {
                    System.out.println("wrong day count");
                    return false;
                }
                if(month <= 0 || month > 12) {
                    System.out.println("wrong month");
                    return false;
                }
                if(year < 1900) {
                    System.out.println("wrong year");
                    return false;
                }
            } catch (Exception e) {
                System.out.println("wrong date");
                return false;
            }
        }

        if(!tripSplit[4].isBlank()) {
            String price = tripSplit[4];
            try {
                float num = Integer.parseInt(price);
                if (num < 0) {
                    System.out.println("wrong price");
                    return false;
                }
            } catch (Exception e) {
                System.out.println("price has to be a number");
                return false;
            }
        }

        if(!tripSplit[5].isBlank()) {
            String vehicle = tripSplit[5].toUpperCase();
            switch (vehicle) {
                case "TRAIN":
                case "BUS":
                case "BOAT":
                case "PLANE":
                    break;
                default:
                    System.out.println("wrong vehicle");
                    return false;
            }
        }

        return true;
    }

    public boolean isAddArgValid(String arg) {
        if(!isEditArgValid(arg)) {
            return false;
        }

        for(String s : arg.split(";")) {
            if(s.isBlank()) {
                System.out.println("You must provide all fields to the trip");
                return false;
            }
        }

        return true;
    }

    public boolean isDelArgValid(String arg) {
        if(arg.isBlank()) {
            System.out.println("This command requires 1 argument");
            return false;
        }

        try {
            int num = Integer.parseInt(arg);

            if(num < 100 || num > 999) {
                System.out.println("Id has to be a 3 digit number");
                return false;
            }
        } catch (Exception ex) {
            System.out.println("Error, command argument has to be a number");
            return false;
        }

        return true;
    }

    public float getValidFindPrice(String arg) {
        if(arg.isBlank()) {
            System.out.println("This command requires 1 argument");
            return -1;
        }
        float price;
        try {
            price = Float.parseFloat(arg);

            if(price < 0) {
                System.out.println("wrong price");
                return -1;
            }
        } catch (Exception ex) {
            System.out.println("Error, command argument has to be a number");
            return -1;
        }
        return price;
    }
}
