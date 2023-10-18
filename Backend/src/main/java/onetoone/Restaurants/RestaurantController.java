package onetoone.Restaurants;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Vivek Bengre
 *
 */

@RestController
public class RestaurantController {

    @Autowired
    RestaurantRepository RestaurantRepository;

    @Autowired
    RestTemplate template = new RestTemplate();

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/restaurant/all")
    List<Restaurant> getAllRestaurant() {
        return RestaurantRepository.findAll();
    }

    @GetMapping(path = "/restaurant/find/{code}")
    Restaurant getRestaurantReviews(@PathVariable String code) {
        return RestaurantRepository.findByCode(code);
    }

    @GetMapping(path = "/restaurant/reviews/{code}")
    @ResponseBody
    JsonNode getRestaurantByName(@PathVariable String code) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.yelp.com/v3/businesses/" + code + "/reviews")
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer MVfL5KGDWbaFAwn7beZaNIdCJZ95r8o09YFJgksy9pN8Q7bgqEhRbJKdtBdLPPmss6xv9mz3s3OTEAAu3oWaCJu5J838o1Aouy68aK2--ugkynfBSbLHKqqfVRr5ZHYx")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                String responseBody = response.body().string();
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readTree(responseBody);
            } else {
                return null;

            }
        }
    }

    @GetMapping(path = "/home/{city}/{price}/{categories}/{attributes}")
    @ResponseBody
    ArrayList<JsonNode> getSwiping(@PathVariable String city, @PathVariable Integer price, @PathVariable String categories,
                                   @PathVariable String attributes) throws IOException {
        ArrayList<JsonNode> restaurants = new ArrayList<JsonNode>();
        categories = categories.replaceAll(",", "%2C");
        attributes = attributes.replaceAll(",", "%20");
        String url = ("https://api.yelp.com/v3/businesses/search?term=restaurants&categories=" + categories + "&attributes=" + attributes);
        for (int i = 0; i < 5; i++) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url + "&location=" + city + "&limit=50&price=" + price + "&offset=" + i)
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer MVfL5KGDWbaFAwn7beZaNIdCJZ95r8o09YFJgksy9pN8Q7bgqEhRbJKdtBd" +
                            "LPPmss6xv9mz3s3OTEAAu3oWaCJu5J838o1Aouy68aK2--ugkynfBSbLHKqqfVRr5ZHYx")
                    .build();
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String responseBody = Objects.requireNonNull(response.body()).string();
                    ObjectMapper objectMapper = new ObjectMapper();
                    if (!restaurants.contains(objectMapper.readTree(responseBody))) {
                        restaurants.add(objectMapper.readTree(responseBody));
                    }
                }
            }
        }
        return restaurants;
    }

    @PutMapping(path = "/restaurant/new-restaurant")
    String createRestaurant(@RequestBody Restaurant Restaurant) {
        if (Restaurant == null)
            return failure;
        RestaurantRepository.save(Restaurant);
        return success;
    }

    @PutMapping("/restaurant/{code}")
    Restaurant updateRestaurant(@PathVariable String code, @RequestBody Restaurant request) {
        Restaurant Restaurant = RestaurantRepository.findByCode(code);
        if (Restaurant == null)
            return null;
        RestaurantRepository.save(request);
        return RestaurantRepository.findByCode(code);
    }

    @DeleteMapping(path = "/restaurant/{code}")
    String deleteRestaurant(@PathVariable String code) {
        RestaurantRepository.deleteByCode(code);
        return success;
    }
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        System.out.println(success);
        return builder.build();
    }
}
