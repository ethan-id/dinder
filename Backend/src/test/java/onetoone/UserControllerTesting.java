package onetoone;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerTesting {

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
    public void testGetAllUsers() throws Exception {
        controller.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username", is("BigE")))
                .andExpect(jsonPath("$[1].username", is("MrEthan")))
                .andExpect(jsonPath("$[2].username", is("Jessticals")));
    }
    @Test
    public void testGetSpecificUser() throws Exception {
    controller.perform(get("/users/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("username", is("BigE")));

        controller.perform(get("/users/2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("username", is("MrEthan")));

        controller.perform(get("/users/3").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("username", is("Jessticals")));
    }
    @Test
    public void testAddingFriends() throws Exception {
        controller.perform(post("/friend/addFriend/BigE,Jessticals").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        controller.perform(post("/friend/addFriend/BigE,MrEthan").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        controller.perform(post("/friend/addFriend/MrEthan,MrEthan").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        controller.perform(post("/friend/addFriend/MrEthan,Jessticals").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        controller.perform(post("/friend/addFriend/Jessticals,BigE").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        controller.perform(post("/friend/addFriend/Jessticals,MrEthan").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void testGetAllFriends() throws Exception {
        controller.perform(get("/friend/Jessticals/getAll").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        controller.perform(get("/friend/MrEthan/getAll").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        controller.perform(get("/friend/BigE/getAll").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void testFindingFriends() throws Exception {
        controller.perform(get("/friend/BigE/find/MrEthan").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        controller.perform(get("/friend/BigE/find/Jessticals").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        controller.perform(get("/friend/MrEthan/find/BigE").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        controller.perform(get("/friend/MrEthan/find/Jessticals").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        controller.perform(get("/friend/Jessticals/find/BigE").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        controller.perform(get("/friend/Jessticals/find/MrEthan").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }


}