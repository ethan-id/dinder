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
     * @param userRepository repository for the User entity
     *                       Creates a commandLine runner to enter dummy data into the database
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
            a.add("Barbeque");a.add("Beer Bar");a.add("Cocktail Bar");
            ArrayList<String> b = new ArrayList<String>();
            b.add("New American");b.add("Breakfast & Brunch");b.add("Bars");
            ArrayList<String> c = new ArrayList<String>();
            c.add("Irish Pub");c.add("Beer Bar");c.add("Traditional American");
            ArrayList<String> d = new ArrayList<String>();
            d.add("New American");
            ArrayList<String> e = new ArrayList<String>();
            e.add("Bakeries");e.add("Breakfast & Brunch");e.add("Coffee & Tea");
            ArrayList<String> f = new ArrayList<String>();
            f.add("Traditional American");f.add("Ice Cream & Frozen Yogurt");
            ArrayList<String> g = new ArrayList<String>();
            g.add("British");g.add("Pubs");g.add("Caterers");
            ArrayList<String> h = new ArrayList<String>();
            h.add("Italian");
            ArrayList<String> i = new ArrayList<String>();
            i.add("Pizza");i.add("Sandwiches");
            ArrayList<String> j = new ArrayList<String>();
            j.add("Mexican");


            restaurantRepository.save(new Restaurant(1, "Cornbread", "kBw1q0aMC5HkJ5vWT_axFA", "false", "4.5", "217", "https://www.yelp.com/biz/cornbred-ames?adjust_creative=RyNyMQd7rvZeoBrbuIrc1w&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=RyNyMQd7rvZeoBrbuIrc1w", "https://s3-media1.fl.yelpcdn.com/bphoto/lwExCed7IisI_koQU1tWKg/o.jpg", "$$", "-93.618123", "42.024457", "526 Main St", "null", "null", a));
            restaurantRepository.save(new Restaurant(2, "Provisions Lot F", "5UQIWWFOGF_3ZC5XLpuUuQ", "false", "4.5", "258", "https://www.yelp.com/biz/provisions-lot-f-ames-2?adjust_creative=RyNyMQd7rvZeoBrbuIrc1w&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=RyNyMQd7rvZeoBrbuIrc1w", "https://s3-media1.fl.yelpcdn.com/bphoto/JHgm0Fz462NAN9lxi7Mpfg/o.jpg", "$$", "-93.63569581557114", "42.00077713820821", "2400 N Loop Dr", "null", "null", b));
            restaurantRepository.save(new Restaurant(3, "Dublin Bay Irish Pub Grill", "PXmhUIYWV6Wa3Ua3UFNV0Q", "false", "4.0", "202", "https://www.yelp.com/biz/dublin-bay-irish-pub-and-grill-ames-3?adjust_creative=RyNyMQd7rvZeoBrbuIrc1w&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=RyNyMQd7rvZeoBrbuIrc1w", "https://s3-media3.fl.yelpcdn.com/bphoto/fHRQNKcHFSMGH05FJX_4-w/o.jpg", "$$", "-93.614776", "42.008025", "320 S 16th St", null, null, c));
            restaurantRepository.save(new Restaurant(4, "Aunt Maude's", "yDf8_64Lp05EXglmcl2Slg", "false", "4.0", "141", "https://www.yelp.com/biz/aunt-maudes-ames?adjust_creative=RyNyMQd7rvZeoBrbuIrc1w&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=RyNyMQd7rvZeoBrbuIrc1w", "https://s3-media2.fl.yelpcdn.com/bphoto/2ooETNT_D8HeFNTeLR1dcQ/o.jpg", "$$", "-93.6190831661224", "42.025307743985", "547 Main St", "(515) 233-4136", "769.0394985467868", d));
            restaurantRepository.save(new Restaurant(5, "The Cafe", "yDf8_64Lp05EXglmcl2Slg", "false", "4.5", "488", "https://www.yelp.com/biz/the-cafe-ames-9?adjust_creative=RyNyMQd7rvZeoBrbuIrc1w&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=RyNyMQd7rvZeoBrbuIrc1w", "https://s3-media1.fl.yelpcdn.com/bphoto/fh2WLCgZOww0XjZcLobiNA/o.jpg", "$$", "-93.64367", "42.04872", "2616 Northridge Pkwy", null, null, e));
            restaurantRepository.save(new Restaurant(6, "Hickory Park Restaurant Co.", "GNAiaFeLzy2J_8S2ZxrybQ", "false", "4.0", "601", "https://www.yelp.com/biz/hickory-park-restaurant-co-ames?adjust_creative=RyNyMQd7rvZeoBrbuIrc1w&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=RyNyMQd7rvZeoBrbuIrc1w", "https://s3-media1.fl.yelpcdn.com/bphoto/YkPVT2SVu9GBwYxlh7acXA/o.jpg", "$$", "-93.6094067755548", "42.0099445811956", "1404 S Duff Ave", "(515) 232-8940", null, f));
            restaurantRepository.save(new Restaurant(7, "The Mucky Duck Pub", "90wYasFlXTjSWsA3BjF4zw", "false", "4.5", "197", "https://www.yelp.com/biz/the-mucky-duck-pub-ames?adjust_creative=RyNyMQd7rvZeoBrbuIrc1w&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=RyNyMQd7rvZeoBrbuIrc1w", "https://s3-media3.fl.yelpcdn.com/bphoto/19s165sggAowG756Ac_CmQ/o.jpg", "$$", "-93.60955112265208", "41.99469728386815", "3100 S Duff Ave", null, null, g));
            restaurantRepository.save(new Restaurant(8, "Bar La Tosca", "8nD2P50BFqaUQAG-4TY36w", "false", "4.0", "60", "https://www.yelp.com/biz/bar-la-tosca-ames?adjust_creative=RyNyMQd7rvZeoBrbuIrc1w&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=RyNyMQd7rvZeoBrbuIrc1w", "https://s3-media4.fl.yelpcdn.com/bphoto/f9dDXNNmqDrHkv8QnZ-aug/o.jpg", "$$", "-93.6156766707959", "42.0249912978246", "303 Welch Ave", "(515) 292-5258", "1042.7453372325815", h));
            restaurantRepository.save(new Restaurant(9, "The Great Plains Sauce & Dough", "8nD2P50BFqaUQAG-4TY36w", "false", "4.0", "186", "https://www.yelp.com/biz/the-great-plains-sauce-and-dough-ames?adjust_creative=RyNyMQd7rvZeoBrbuIrc1w&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=RyNyMQd7rvZeoBrbuIrc1w", "https://s3-media2.fl.yelpcdn.com/bphoto/iorgtqKIwAZHnslqa7DQBg/o.jpg", "$$", "-93.6118147", "42.0253575", "129 Main St", null, null, i));
            restaurantRepository.save(new Restaurant(10, "La Casa Maya", "yPGjbSl_vQKPekYfCT9VJw", "false", "4.5", "17", "https://www.yelp.com/biz/la-casa-maya-ames?adjust_creative=RyNyMQd7rvZeoBrbuIrc1w&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=RyNyMQd7rvZeoBrbuIrc1w", "https://s3-media1.fl.yelpcdn.com/bphoto/CR6HFfVrgrOJSW1ZEp2oMw/o.jpg", "$$", "-93.61941367", "42.02364783", "631 Lincoln Way", "(515) 233-7179", "724.4502904006047", j));

        };
    }
}
