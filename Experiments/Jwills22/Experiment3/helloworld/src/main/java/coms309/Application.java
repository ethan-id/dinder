package coms309;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * PetClinic Spring Boot Application.
 *
 * @author Vivek Bengre
 */

@SpringBootApplication
public class Application {


        public static void main(String[] args) throws FileNotFoundException, Exception {
            ArrayList<String> n = new ArrayList<String>();
            Scanner s = new Scanner(System.in);
            System.out.println("\nWhat restaurant would you like more information on?\n");
            String response = s.nextLine();
            s.close();
            n = RetrieveFromFile(response, 3.0);
            WelcomeController x = new WelcomeController();
            x.ArrayListImport(n, response);
            SpringApplication.run(Application.class, args);
        }

        public static ArrayList<String> RetrieveFromFile(String input, double RATING_INDEX) throws FileNotFoundException {
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
            reader.close();

            File file2 = new File("C:/Dinder/Dinder/sd_322/Experiments/Jwills22/Experiment2/response2.json");
            Scanner reader2 = new Scanner(new FileReader(file2));
            reader2.useDelimiter(",");

            while (reader2.hasNextLine()) {
                a.add(reader2.next());
            }
            reader2.close();

            File file3 = new File("C:/Dinder/Dinder/sd_322/Experiments/Jwills22/Experiment2/response3.json");
            Scanner reader3 = new Scanner(new FileReader(file3));
            reader3.useDelimiter(",");

            while (reader3.hasNextLine()) {
                a.add(reader3.next());
            }
            reader3.close();

            File file4 = new File("C:/Dinder/Dinder/sd_322/Experiments/Jwills22/Experiment2/response4.json");
            Scanner reader4 = new Scanner(new FileReader(file4));
            reader4.useDelimiter(",");

            while (reader4.hasNextLine()) {
                a.add(reader4.next());
            }
            reader4.close();
            for (i = 0; i < a.size(); i++) {
                if (a.get(i).contains("\"name\":")) {
                }
                if (a.get(i).substring(1).equals(response)) {
                    for (int j = i; j < (i + 26); j++) {
                        if (a.get(j).contains("url") && !b.contains("url")) {
                            url = a.get(j).substring(8);
                            b.add("\n" + input + "'s url: " + url);
                        } else if (a.get(j).contains("rating") && !b.contains("rating")) {
                            rating = a.get(j).substring(11);
                            b.add(input + "'s rating: " + rating);
                        } else if (a.get(j).contains("price") && !b.contains("price")) {
                            price = a.get(j).substring(11, 12);
                            b.add(input + "'s price: " + price);
                        } else if (a.get(j).contains("location") && (!b.contains("location") || !b.contains("address"))) {
                            address = a.get(j).substring(27, 38);
                            b.add(input + "'s address: " + address);
                        } else if (a.get(j).contains("phone") && !b.contains("phone")) {
                            phoneNumber = a.get(j).substring(10);
                            b.add(input + "'s phone number: " + phoneNumber);
                        }
                    }
                }
            }
            return b;
        }

    }

