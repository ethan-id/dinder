package onetoone;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import onetoone.Users.UserRepository;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableCaching
@SpringBootApplication
@ComponentScan(basePackages = {"onetoone.websocket", "onetoone.Restaurants", "onetoone.Users", "onetoone.Likes", "onetoone.Requests", "onetoone.Favorites", "onetoone.Statistics"})
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
    CommandLineRunner initUser(UserRepository userRepository) {
        return args -> {};
    }
}
