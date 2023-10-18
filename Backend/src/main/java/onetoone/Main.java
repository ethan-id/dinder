package onetoone;

import onetoone.Restaurants.Restaurant;
import onetoone.Restaurants.RestaurantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import onetoone.Users.User;
import onetoone.Users.UserRepository;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;


@SpringBootApplication
@ComponentScan(basePackages = {"onetoone.websocket", "onetoone.restaurants", "onetoone.users"})
//@EnableJpaRepositories
class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
//        SpringApplication.run(WebSocketSpringbootApplication.class, args);
    }


    /**
     * @param userRepository repository for the User entity
     *                       Creates a commandLine runner to enter dummy data into the database
     */
    @Bean
    CommandLineRunner initUser(UserRepository userRepository, RestaurantRepository restaurantRepository) {
        return args -> {};
    }
}
