package onetoone.Restaurants;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
/**
 *
 * @author Vivek Bengre
 *
 */

@RestController
public class RestaurantController {

    @Autowired
    RestaurantRepository RestaurantRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/All/Restaurant")
    List<Restaurant> getAllRestaurant(){
        return RestaurantRepository.findAll();
    }

    @GetMapping(path = "/Restaurant/{id}")
    Restaurant getRestaurantById(@PathVariable int id){
        return RestaurantRepository.findById(id);
    }

    @PostMapping(path = "/Restaurant")
    String createRestaurant(Restaurant Restaurant){
        if (Restaurant == null)
            return failure;
        RestaurantRepository.save(Restaurant);
        return success;
    }

    @PutMapping("/Restaurant/{id}")
    Restaurant updateRestaurant(@PathVariable int id, @RequestBody Restaurant request){
        Restaurant Restaurant = RestaurantRepository.findById(id);
        if(Restaurant == null)
            return null;
        RestaurantRepository.save(request);
        return RestaurantRepository.findById(id);
    }

    @DeleteMapping(path = "/Restaurant/{id}")
    String deleteRestaurant(@PathVariable int id){
        RestaurantRepository.deleteById(id);
        return success;
    }
}
