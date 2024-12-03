import java.io.*;
import java.util.Scanner;
import java.util.Vector;

public class Main {

    static Scanner sc = new Scanner(System.in);

    static String filename = "db.csv";
    static String arg = "";

    static ArgValidator validator = new ArgValidator();
    static Utils utils = new Utils();

    public static void main(String[] args) {
        String[] commandLine;
        print();
        loop: while (true) {
            commandLine = sc.nextLine().split(" ");
            String command = commandLine[0];

            if (commandLine.length > 1) {
                arg = commandLine[1];
            } else {
                arg= "";
            }
             switch (command) {
                    case "print":
                        print();
                        break;
                    case "sort":
                        sort();
                        break;
                    case "add":
                        add();
                        break;
                    case "edit":
                        edit();
                        break;
                    case "del":
                        del();
                        break;
                    case "about":
                        about();
                    case "find":
                        find();
                        break;
                    case "avg":
                        avg();
                        break;
                    case "exit":
                        break loop;
                    default:
                        System.out.println("Unknown command");
                        break;
            }
        }
        sc.close();
    }

    public static void printTripLine(String[] line) {
        System.out.printf("\n%-4s%-21s%-11s%6s%10s %-7s", line[0], line[1], line[2], line[3], line[4], line[5]);
    }

    public static void printTableHeader() {
        System.out.println("\n------------------------------------------------------------");
        System.out.printf("%-4s%-21s%-11s%6s%10s %-7s", "ID", "City", "Date", "Days", "Price", "Vehicle");
        System.out.print("\n------------------------------------------------------------");
    }
    public static void printTableFooter() {;
        System.out.print("\n------------------------------------------------------------\n");
    }

    public static void print() {
        if(!utils.isDbFileExists(filename)) {
            return;
        }

        printTableHeader();

        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            while ((line = br.readLine()) != null) {
                String[] trip = line.split(";");
                printTripLine(trip);
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        printTableFooter();
    }

    public static void add() {
        if(!validator.isAddArgValid(arg) || !utils.isDbFileExists(filename)) {
            return;
        }

        try {
            PrintWriter out = new PrintWriter(new FileWriter(filename, true));
            out.append("\n").append(utils.formatTripFields(arg));
            out.close();
        } catch (Exception ex) {
            System.out.println("Error during adding: " + ex.getMessage());
        }
        System.out.println("added");
    }

    public static void edit() {
        if(!validator.isEditArgValid(arg) || !utils.isDbFileExists(filename)) {
            return;
        }

        String[] tripSplit = arg.split(";");
        String id = tripSplit[0];

        File f = new File(filename);
        File tempFile = new File("temp.txt");

        boolean isIdFound = false;

        try {
            Scanner in = new Scanner(f);
            PrintWriter out = new PrintWriter(new FileWriter(tempFile));

            String line;
            Vector<String> trips = new Vector<>();

            while (in.hasNextLine()) {
                line = in.nextLine();
                trips.addElement(line);
            }

            for (String t : trips) {
                String[] tSplit = t.split(";");
                if(tSplit[0].equals(id)) {
                    isIdFound = true;
                    StringBuilder outS = new StringBuilder();

                    for (int i = 0; i < tSplit.length; i++) {
                        if(tripSplit[i].isBlank()) {
                            outS.append(tSplit[i]);
                        } else {
                            System.out.println(tripSplit[i]);
                            outS.append(tripSplit[i]);
                        }

                        if (i != tSplit.length - 1) {
                            outS.append(";");
                        }
                    }
                    out.println(utils.formatTripFields(outS.toString()));
                    continue;
                }
                out.println(t);
            }

            in.close();
            out.close();

            System.gc();

            if(!isIdFound) {
                System.out.println("wrong id");
            }
            if (f.delete()) {
                if (tempFile.renameTo(f)) {
                    if(isIdFound) {
                        System.out.println("changed");
                    }
                } else {
                    System.out.println("Failed to rename temp file");
                }
            } else {
                System.out.println("Failed to edit");
            }

        } catch (Exception ex) {
            System.out.println("Error during editing: " + ex.getMessage());
        }
    }

    public static void sort() {
        if(!utils.isDbFileExists(filename)) {
            return;
        }

        File f = new File(filename);
        File tempFile = new File("temp.txt");

        try {
            Scanner in = new Scanner(f);
            PrintWriter out = new PrintWriter(new FileWriter(tempFile));

            String line;
            Vector<String> trips = new Vector<>();

            while (in.hasNextLine()) {
                line = in.nextLine();
                trips.addElement(line);
            }

            trips.sort((s1, s2) -> {
                String[] date1 = s1.split(";")[2].split("/");
                String[] date2 = s2.split(";")[2].split("/");

                int[] intDate1 = new int[date1.length];
                int[] intDate2 = new int[date1.length];

                for (int i = 0; i < date1.length; i++) {
                    intDate1[i] = Integer.parseInt(date1[i]);
                    intDate2[i] = Integer.parseInt(date2[i]);
                }

                int numDate1 = intDate1[0] + intDate1[1] * 31 + intDate1[2] * 365;
                int numDate2 = intDate2[0] + intDate2[1] * 31 + intDate2[2] * 365;
                return numDate1 - numDate2;
            });

            boolean isFirst = true;
            for (String trip : trips) {
                if(isFirst) {
                    isFirst = false;
                } else {
                    out.print("\n");
                }
                out.print(trip);
            }

            in.close();
            out.close();

            System.gc();
            if (f.delete()) {
                if (tempFile.renameTo(f)) {
                    System.out.println("sorted");
                } else {
                    System.out.println("Failed to rename temp file");
                }
            } else {
                System.out.println("Failed to delete original file");
            }

        } catch (Exception ex) {
            System.out.println("Error during sorting: " + ex.getMessage());
        }
    }

    static public  void find() {
        float price = validator.getValidFindPrice(arg);
        if(price == -1 || !utils.isDbFileExists(filename)) {
            return;
        }

        File f = new File(filename);

        printTableHeader();
        try {
            Scanner in = new Scanner(f);

            String line;
            Vector<String> trips = new Vector<>();

            while (in.hasNextLine()) {
                line = in.nextLine();
                trips.addElement(line);
            }

            for (String trip : trips) {
                float tripPrice = Float.parseFloat(trip.split(";")[4]);


                if(tripPrice < price) {
                    printTripLine(trip.split(";"));
                }
            }

            in.close();
            printTableFooter();

        } catch (Exception ex) {
            System.out.println("Error during find: " + ex.getMessage());
        }
    }

    static public  void avg() {
        File f = new File(filename);
        if(!utils.isDbFileExists(filename)) {
            return;
        }

        float avg = 0;
        try {
            Scanner in = new Scanner(f);

            String line;
            Vector<String> trips = new Vector<>();

            while (in.hasNextLine()) {
                line = in.nextLine();
                trips.addElement(line);
            }

            for (String trip : trips) {
                float tripPrice = Float.parseFloat(trip.split(";")[4]);
                avg += tripPrice;
            }
            avg /= trips.size();

            in.close();

        } catch (Exception ex) {
            System.out.println("Error during find: " + ex.getMessage());
        }

        System.out.printf("average=%.2f", avg);
    }


    public static void del() {
        if(!validator.isDelArgValid(arg) || !utils.isDbFileExists(filename)) {
            return;
        }

        String id = arg;

        File f = new File(filename);
        File tempFile = new File("temp.txt");

        boolean isIdFound = false;

        try {
            Scanner in = new Scanner(f);
            PrintWriter out = new PrintWriter(new FileWriter(tempFile));

            String line;
            Vector<String> trips = new Vector<>();

            while (in.hasNextLine()) {
                line = in.nextLine();
                trips.addElement(line);
            }

            for (String trip : trips) {
                if(!trip.split(";")[0].equals(id)) {
                    out.println(trip);
                } else {
                    isIdFound = true;
                }
            }

            in.close();
            out.close();

            System.gc();

            if(!isIdFound) {
                System.out.println("wrong id");
            }
            if (f.delete()) {
                if (tempFile.renameTo(f)) {
                    if(isIdFound) {
                        System.out.println("deleted");
                    }
                } else {
                    System.out.println("Failed to rename temp file");
                }
            } else {
                System.out.println("Failed to delete original file");
            }

        } catch (Exception ex) {
            System.out.println("Error during deletion: " + ex.getMessage());
        }
    }

    public static void about() {
        System.out.println("Developed by Ņikita Obrazcovs 241RDB190");
    }
}
