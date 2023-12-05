package onetoone;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTesting {

    @Autowired
    private MockMvc controller;

    @Test
    public void testGetAllUsers() throws Exception {
        controller.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username", is("user1"))); // Example: Checking username of first user
    }



//    @Test
//    public void testCapitalizeEndpoint() throws Exception {
//        // CALL THE CONTROLLER DIRECTLY (instead of using postman) AND TEST THE RESULTS
//        // we sent hello. we expect back a list with first object having "olleh" in it
//        controller.perform(post("/capitalize").contentType(MediaType.APPLICATION_JSON).content("hello"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[1].data", is("HELLO")));
//    }

}