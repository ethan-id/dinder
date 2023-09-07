        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.FileReader;
        import java.util.Scanner;
        import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws FileNotFoundException{
        String n;
        Scanner s = new Scanner(System.in);
        System.out.println("\nWhere would you like to eat?\nOr type \"all\" to see all the restaurants above a 4.5 rating");
        String response = s.nextLine();
        s.close();
        n = RetrieveFromFile(response, 4.5);
        if (n.equals("Restaurant not in radius")) {
            System.out.println("\n"+"Restaurant not in radius");
        }
        else {
            System.out.println("\n" + "The rating of " + response + " is: " + n);
        }
    }

    public static String RetrieveFromFile(String input, double RATING_INDEX) throws FileNotFoundException {
        String rating = "Restaurant not in radius";
        int i = 0;

        String response = "\"name\": \"" + input + "\"";
        ArrayList<String> a = new ArrayList<String>();

        File file = new File("C:/Dinder/Dinder/sd_322/Experiments/Jwills22/Experiment2/response.json");
        Scanner reader = new Scanner(new FileReader(file));
        reader.useDelimiter(",");

        while (reader.hasNextLine()) {
            a.add(reader.next());
        }
        for (i = 0; i < a.size(); i++) {
            if (a.get(i).substring(1).equals(response)) {
                for (int j = i; j < a.size(); j++) {
                    if (a.get(j).contains("rating")) {
                        rating = a.get(j).substring(11);
                        return rating;
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
        return rating;
    }
}
