package onetoone;

import onetoone.Restaurants.Restaurant;
import onetoone.Restaurants.RestaurantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import onetoone.Users.User;
import onetoone.Users.UserRepository;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;


@SpringBootApplication
//@EnableJpaRepositories
class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    // Create 3 users with their machines
    /**
     * 
     * @param userRepository repository for the User entity
     * Creates a commandLine runner to enter dummy data into the database
     * As mentioned in User.java just associating the Laptop object with the User will save it into the database because of the CascadeType
     */
    @Bean
    CommandLineRunner initUser(UserRepository userRepository, RestaurantRepository restaurantRepository) {
            return args -> {
                User user1 = new User("Eli", "BigE", "1234");
                User user2 = new User("Ethan", "MrEthan", "johndeere");
                User user3 = new User("Jesse", "Jessticals", "hehe");
                userRepository.save(user1);
                userRepository.save(user2);
                userRepository.save(user3);

                ArrayList<String> a = new ArrayList<String>();
                int counter = 0;
                int i;
                /**
                 * This delimits all the files that were returned from the call from Postman. 5 files, each one contains 50 restaurants except the last one
                 * It adds a comma and \n after every value for easy parsing later
                 */
                File file = new File("response.json");
                Scanner reader = new Scanner(new FileReader(file));
                reader.useDelimiter(",");

                while (reader.hasNextLine()) {
                    a.add(reader.next());
                }
                reader.close();

                File file2 = new File("response2.json");
                Scanner reader2 = new Scanner(new FileReader(file2));
                reader2.useDelimiter(",");

                while (reader2.hasNextLine()) {
                    a.add(reader2.next());
                }
                reader2.close();

                File file3 = new File("response3.json");
                Scanner reader3 = new Scanner(new FileReader(file3));
                reader3.useDelimiter(",");

                while (reader3.hasNextLine()) {
                    a.add(reader3.next());
                }
                reader3.close();

                File file4 = new File("response4.json");
                Scanner reader4 = new Scanner(new FileReader(file4));
                reader4.useDelimiter(",");

                while (reader4.hasNextLine()) {
                    a.add(reader4.next());
                }
                reader4.close();

                File file5 = new File("response5.json");
                Scanner reader5 = new Scanner(new FileReader(file5));
                reader5.useDelimiter(",");

                while (reader5.hasNextLine()) {
                    a.add(reader5.next());
                }
                reader5.close();
                /**
                 * This portion of the method is iterating through the parsed .JSON file and finding the required parameters for the
                 * Constructor for class Restaurant
                 */
                for (i = 0; i < a.size(); i++) {
                    Restaurant x = new Restaurant();
                    if (a.get(i).contains("{\"id\"")) {
                        counter++;
                        for (int j = i; j < (i + 28); j++) {
                            if (a.get(j).contains("name")) {
                                x.set_name(a.get(j).substring(8));
                            } else if (a.get(j).contains("id")) {
                                x.set_code(a.get(j).substring(8));
                            } else if (a.get(j).contains("is_closed")) {
                                x.set_is_closed(a.get(j).substring(14));
                            } else if (a.get(j).contains("rating")) {
                                x.set_rating(a.get(j).substring(11));
                            } else if (a.get(j).contains("review_count")) {
                                x.set_review_count(a.get(j).substring(17));
                            } else if (a.get(j).contains("longitude")) {
                                x.set_longitude(a.get(j).substring(13, a.get(j).length() - 1));
                            } else if (a.get(j).contains("latitude")) {
                                x.set_latitude(a.get(j).substring(28));
                            } else if (a.get(j).contains("image_url")) {
                                x.set_image_url(a.get(j).substring(14));
                            } else if (a.get(j).contains("url")) {
                                x.set_url(a.get(j).substring(8));
                            } else if (a.get(j).contains("stan")) {
                                x.set_distance(a.get(j).substring(13));
                            } else if (a.get(j).contains("play_ph")) {
                                x.set_phone_number(a.get(j).substring(19));
                            } else if (a.get(j).contains("play_addr")) {
                                x.set_address(a.get(j).substring(22));
                            } else if (a.get(j).contains("price")) {
                                x.set_price(a.get(j).substring(9));
                            }
                        }
                    }
                    /**
                     * This if statement is necessary because every iteration of the for loop it creates a new restaurant
                     * regardless if the values get assigned
                     **/
                    if (x.get_name() != null) {
                        x.set_count(counter);
                        if (x.get_price() == null) {
                            x.set_price("$");
                        }
                        //System.out.println(x.printValues());
                        restaurantRepository.save(x);
                    }
                }

            };
        };
    }
