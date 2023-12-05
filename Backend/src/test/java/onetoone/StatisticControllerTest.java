package onetoone;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StatisticControllerTest {

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
    public void testGetTotalLikes() throws Exception{
        controller.perform(get("/stats/get/Likes").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void testGetTotalUsers() throws Exception{
        controller.perform(get("/stats/get/Users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void testGetTotalFavorites() throws Exception{
        controller.perform(get("/stats/get/Favorites").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void testAvgSwipesBeforeMatch() throws Exception{
        controller.perform(get("/stats/get/SwipesBeforeMatch").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void testAvgLikesPerMatch() throws Exception{
        controller.perform(get("/stats/get/LikesPerMatch").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void testAvgSwipesBeforeLike() throws Exception{
        controller.perform(get("/stats/get/SwipesBeforeLike").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void testAvgLikesPerUser() throws Exception{
        controller.perform(get("/stats/get/LikesPerUser").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void testAvgMatchesPerUser() throws Exception{
        controller.perform(get("/stats/get/MatchesPerUser").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}
