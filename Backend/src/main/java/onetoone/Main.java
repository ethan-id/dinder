package onetoone;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import onetoone.Users.User;
import onetoone.Users.UserRepository;


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
    CommandLineRunner initUser(UserRepository userRepository) {
        return args -> {
            User user1 = new User("Eli", "BigE", "1234");
            User user2 = new User("Ethan", "MrEthan", "johndeere");
            User user3 = new User("Jesse", "Jessticals", "hehe");
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
        };
    }

}
