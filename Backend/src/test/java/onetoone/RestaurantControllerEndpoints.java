package onetoone;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RestaurantControllerEndpoints {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Autowired
    private MockMvc controller;

    @Test
    public void testRestaurantAll() throws Exception {
        controller.perform(get("/restaurant/Ames/all").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].businesses[0].id", is("kBw1q0aMC5HkJ5vWT_axFA"))) // Example: Checking username of first user
                .andExpect(jsonPath("$[0].businesses[0].name", is("Cornbred"))); // Example: Checking username of first user
    }
    @Test
    public void testRestaurantFind() throws Exception {
        controller.perform(get("/restaurant/find/90wYasFlXTjSWsA3BjF4zw").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("special_hours[0].date", is("2023-12-24")));
    }
    @Test
    public void testRestaurantReviews() throws Exception {
        controller.perform(get("/restaurant/reviews/90wYasFlXTjSWsA3BjF4zw").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("reviews[0].text", is("Stopped in for lunch and exceeded expectations.  Had the fish and chips.  Super crispy and could not stop eating the chips.  Husband had Sheppards pie and...")));
    }
    @Test
    public void testGetSwiping() throws Exception {
        controller.perform(get("/home/NYC/1/bagels/vegan").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].businesses[0].categories[0].title", is("Bagels")));
    }
}
