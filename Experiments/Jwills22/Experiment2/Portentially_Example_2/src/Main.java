        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.FileReader;
        import java.util.Scanner;
        import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws FileNotFoundException{
        String n;
        Scanner s = new Scanner(System.in);
        System.out.println("\nWhere would you like to eat?\nOr type \"all\" to see all the restaurants above a 3.0 rating");
        String response = s.nextLine();
        s.close();
        RetrieveFromFile(response, 3.0);
    }

    public static void RetrieveFromFile(String input, double RATING_INDEX) throws FileNotFoundException {
        String rating = "Restaurant not in radius";
        String phoneNumber = "";
        String address = "";
        String price = "";
        String url = "";
        int i = 0;

        String response = "\"name\": \"" + input + "\"";
        ArrayList<String> a = new ArrayList<String>();
        ArrayList<String> b = new ArrayList<String>();

        File file = new File("C:/Dinder/Dinder/sd_322/Experiments/Jwills22/Experiment2/response.json");
        Scanner reader = new Scanner(new FileReader(file));
        reader.useDelimiter(",");

        while (reader.hasNextLine()) {
            a.add(reader.next());
        }
        for (i = 0; i < a.size(); i++) {
            if (a.get(i).substring(1).equals(response)) {
                for (int j = i; j < (i+26); j++) {
                    if (a.get(j).contains("url") && !b.contains("url")) {
                        url = a.get(j).substring(8);
                        b.add("\n" + input + "'s url: " + url);
                    }
                    else if (a.get(j).contains("rating")&& !b.contains("rating")) {
                        rating = a.get(j).substring(11);
                        b.add(input + "'s rating: " + rating);
                    }
                    else if (a.get(j).contains("price")&& !b.contains("price")) {
                        price = a.get(j).substring(11, 12);
                        b.add(input + "'s price: " + price);
                    }
                    else if (a.get(j).contains("location") && (!b.contains("location") || !b.contains("address"))) {
                        address = a.get(j).substring(27, 38);
                        b.add(input + "'s address: " + address);
                    }
                    else if (a.get(j).contains("phone")&& !b.contains("phone")) {
                        phoneNumber = a.get(j).substring(10);
                        b.add(input + "'s phone number: " + phoneNumber);
                    }
                }
            }
            else if (a.get(i).contains("name") && response.equals("\"name\": \"all\"")) {
                for (int j = i; j < a.size(); j++) {
                    if (a.get(j).contains("rating")) {
                        String allRating = "rating = " + a.get(j).substring(11);
                        if (Double.parseDouble(allRating.substring(9)) >= RATING_INDEX) {
                            System.out.println(a.get(i).substring(1));
                            System.out.println(allRating);
                            System.out.println();
                        }

                        break;
                    }
                }
            }
        }
        for (i = 0; i < b.size(); i++) {
            System.out.println(b.get(i));
        }
    }
}
