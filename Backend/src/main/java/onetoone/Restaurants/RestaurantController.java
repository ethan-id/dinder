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

    @GetMapping(path = "/restaurant/all")
    List<Restaurant> getAllRestaurant(){
        return RestaurantRepository.findAll();
    }

    @GetMapping(path = "/restaurant/find/{code}")
    Restaurant getRestaurantById(@PathVariable String code){
        return RestaurantRepository.findByCode(code);
    }

    @PutMapping(path = "/restaurant/new-restaurant")
    String createRestaurant(@RequestBody Restaurant Restaurant){
        if (Restaurant == null)
            return failure;
        RestaurantRepository.save(Restaurant);
        return success;
    }

    @PutMapping("/restaurant/{code}")
    Restaurant updateRestaurant(@PathVariable String code, @RequestBody Restaurant request){
        Restaurant Restaurant = RestaurantRepository.findByCode(code);
        if(Restaurant == null)
            return null;
        RestaurantRepository.save(request);
        return RestaurantRepository.findByCode(code);
    }

    @DeleteMapping(path = "/restaurant/{code}")
    String deleteRestaurant(@PathVariable String code){
        RestaurantRepository.deleteByCode(code);
        return success;
    }
}
