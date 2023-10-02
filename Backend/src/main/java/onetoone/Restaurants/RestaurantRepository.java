package onetoone.Restaurants;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Vivek Bengre
 *
 */

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Restaurant findById(int id);

    void deleteById(int id);

}
