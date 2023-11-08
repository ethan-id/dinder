package onetoone;

import onetoone.Restaurants.RestaurantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import onetoone.Users.UserRepository;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"onetoone.websocket", "onetoone.Restaurants", "onetoone.Users", "onetoone.Likes"})
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
