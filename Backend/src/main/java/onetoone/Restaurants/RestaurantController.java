package onetoone.Restaurants;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import okhttp3.*;
import onetoone.websocket.ChatServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Jesse Williams
 *
 */

@Api(value = "RestaurantController", description = "REST API's related to restaurants and receiving information from Yelp's API about them")
@RestController
public class RestaurantController {

    String JesseKey = "Bearer MVfL5KGDWbaFAwn7beZaNIdCJZ95r8o09YFJgksy9pN8Q7bgqEhRbJKdtBdLPPmss6xv9mz3s3OTEAAu3oWaCJu5J838o1Aouy68aK2--ugkynfBSbLHKqqfVRr5ZHYx";
    String EliKey = "Bearer tBTnB2sqqEgwDw8eWPa3VoOhvXZAd-wCEQ6qKzocvrknlkmD4e-8wvQzDFWghKQKAWe1KGFyhL7j-6bb9JYjHpPJ9h2cApdhsSPdwMUZlOKHUjUhSaIL4RvR9sVCZXYx";
    String EthanKey = "Bearer WCsvt3PJkVizdHqlMBf8vnsfb0sA5z7LN0d8c8edwj8H1idNi0Zav0Qm4ZntAW5sv8TfdjLL7C30kLYLKQUxPAQmW-nNb2GDj_WRxq3AW4NFylBXSGJXPc0TGE1uZXYx";
    String LaliKey = "Bearer FhNQ_ij5Fs-nZRXlXrAbTDPVM38T3qNH1R3HkioxTm2u9j3Z07aKzQFDYO0_XRDFU11NlT2fTY-thzpb1C1TF_1nzFVx1qmjvLmsexGvOIVpr9bXy3XRcEv7mI1uZXYx";
    @Autowired
    RestaurantRepository RestaurantRepository;

    private final Logger logger = LoggerFactory.getLogger(RestaurantController.class);

    @Autowired
    RestTemplate template = new RestTemplate();

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @ApiOperation(value = "Get all restaurants in the user-specified city, limit is 50", response = Iterable.class, tags = "getRestaurantsInCity")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "Not authorized!"),
            @ApiResponse(code = 403, message = "Forbidden!"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 510, message = "Internal server error")})
    @GetMapping(path="/restaurant/{city}/all")
    @ResponseBody
    @NonNull
    ArrayList<JsonNode> getAllRestaurants(@PathVariable String city) {
        ArrayList<JsonNode> restaurants = new ArrayList<JsonNode>();
        String url = ("https://api.yelp.com/v3/businesses/search?&limit=50&term=restaurants&location=" + city);
        for (int i = 0; i < 5; i++) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url + "&offset=" + i)
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", LaliKey)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseBody = Objects.requireNonNull(response.body()).string();
                    ObjectMapper objectMapper = new ObjectMapper();
                    if (!restaurants.contains(objectMapper.readTree(responseBody))) {
                        restaurants.add(objectMapper.readTree(responseBody));
                    }
                }
            }
            catch (IOException e) {
                logger.info(Arrays.toString(e.getStackTrace()));
            }
        }
        return restaurants;
    }
    @ApiOperation(value = "Find a specific restaurant by their unique code", response = Iterable.class, tags = "FindRestaurantByCode")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "Not authorized!"),
            @ApiResponse(code = 403, message = "Forbidden!"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 510, message = "Internal server error")})
    @GetMapping(path = "/restaurant/find/{code}")
    @ResponseBody
    JsonNode findRestaurantByCode(@PathVariable String code) throws JsonProcessingException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.yelp.com/v3/businesses/" + code)
                .addHeader("accept", "application/json")
                .addHeader("Authorization",LaliKey)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseBody = Objects.requireNonNull(response.body()).string();
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readTree(responseBody);
            } else {
                logger.info("Response was unsuccessful");
                return new ObjectMapper().readTree("Response was unsuccessful");
            }
        }
        catch (IOException e) {
            logger.info(Arrays.toString(e.getStackTrace()));
            System.out.println("Build unsuccessful");
            return new ObjectMapper().readTree("Build unsuccessful");
        }
    }

    @ApiOperation(value = "Find a specific restaurant's reviews, limit is 3", response = Iterable.class, tags = "GetRestaurantReviews")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "Not authorized!"),
            @ApiResponse(code = 403, message = "Forbidden!"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 510, message = "Internal server error")})
    @GetMapping(path = "/restaurant/reviews/{code}")
    @ResponseBody
    JsonNode getRestaurantByReviews(@PathVariable String code) throws JsonProcessingException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.yelp.com/v3/businesses/" + code + "/reviews")
                .addHeader("accept", "application/json")
                .addHeader("Authorization", LaliKey)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseBody = Objects.requireNonNull(response.body()).string();
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readTree(responseBody);
            } else {
                logger.info("Response was unsuccessful");
                return new ObjectMapper().readTree("Response was unsuccessful");
            }
        }
        catch (IOException e) {
            System.out.println("Build unsuccessful");
            return new ObjectMapper().readTree("Build unsuccessful");
        }
    }
    @ApiOperation(value = "Allows the user to completely customize the restaurants they see. They can update their location, radius, " +
            "update their accomodations and preferences, will auto update the url based on the user's wants", response = Iterable.class, tags = "CustomURLCallforUser")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "Not authorized!"),
            @ApiResponse(code = 403, message = "Forbidden!"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 510, message = "Internal server error")})
    @GetMapping(path = "/home/{city}/{price}/{categories}/{attributes}")
    @ResponseBody
    ArrayList<JsonNode> getSwiping(@PathVariable String city, @PathVariable Integer price, @PathVariable String categories,
                                   @PathVariable String attributes) throws IOException {
        ArrayList<JsonNode> restaurants = new ArrayList<JsonNode>();
        categories = categories.replaceAll(",", "%2C");
        attributes = attributes.replaceAll(",", "%20");
        String url = ("https://api.yelp.com/v3/businesses/search?&limit=50&term=restaurants&categories=" + categories + "&attributes=" + attributes);
        for (int i = 0; i < 5; i++) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url + "&location=" + city + "&price=" + price + "&offset=" + i)
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", LaliKey)
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

    @ApiOperation(value = "Create a new restaurant object", response = Iterable.class, tags = "CreateRestaurant")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "Not authorized!"),
            @ApiResponse(code = 403, message = "Forbidden!"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 510, message = "Internal server error")})
    @PutMapping(path = "/restaurant/new-restaurant")
    String createRestaurant(@RequestBody Restaurant Restaurant) {
        if (Restaurant == null)
            return failure;
        RestaurantRepository.save(Restaurant);
        return success;
    }

    @ApiOperation(value = "Update a restaurant object", response = Iterable.class, tags = "UpdateRestaurant")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "Not authorized!"),
            @ApiResponse(code = 403, message = "Forbidden!"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 510, message = "Internal server error")})
    @PutMapping("/restaurant/{code}")
    Restaurant updateRestaurant(@PathVariable String code, @RequestBody Restaurant request) {
        Restaurant Restaurant = RestaurantRepository.findByCode(code);
        if (Restaurant == null)
            return null;
        RestaurantRepository.save(request);
        return RestaurantRepository.findByCode(code);
    }

    @ApiOperation(value = "Delete a restaurant object from the database", response = Iterable.class, tags = "DeleteRestaurant")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "Not authorized!"),
            @ApiResponse(code = 403, message = "Forbidden!"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 510, message = "Internal server error")})
    @DeleteMapping(path = "/restaurant/{code}")
    String deleteRestaurant(@PathVariable String code) {
        RestaurantRepository.deleteByCode(code);
        return success;
    }
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
