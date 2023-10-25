package onetoone.Favorites;

import onetoone.Restaurants.Restaurant;
import onetoone.Users.User;
import org.hibernate.annotations.Entity;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;

@Entity
public class Favorite {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        private User user;

        @ManyToOne
        private Restaurant restaurant;

        // Constructors, getters, setters
    }


