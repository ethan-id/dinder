package onetoone;

import static io.restassured.RestAssured.delete;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RequestControllerTest {

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
    @Transactional
    @Rollback
    public void testCreateNewRequest() throws Exception {
        controller.perform(post("/request/create/BigE/friend/MrEthan").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        controller.perform(post("/request/create/MrEthan/friend/Jessticals").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        controller.perform(post("/request/create/Jessticals/friend/MrEthan").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
   @Test
   @Transactional
   @Rollback
    public void testAcceptingRequests() throws Exception {
        controller.perform(post("/request/accept/156").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    @Rollback
    @Transactional
    public void testDeletingRequests() throws Exception {
        int userId = 156;
        controller.perform(MockMvcRequestBuilders.delete("/users/{id}", userId))
                .andExpect(status().isOk());
    }

}
