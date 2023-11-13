package onetoone.Requests;

import onetoone.Users.User;
import onetoone.websocket.ChatServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class RequestController {

    public String success = "Success!";
    public String failure = "Failure";
    public String exists = "Already exists.";
    @Autowired
    RequestRepository requestRepository;

    private final Logger logger = LoggerFactory.getLogger(ChatServer.class);
    @PostMapping(path = "/request/create/{parameter}/{creator}/{invited}")
    public String createNewRequest(@PathVariable String parameter, @PathVariable User invited) {
        if (requestRepository.findByParameter(parameter) != null) {
            return exists;
        }
        try {
            Request request = new Request(parameter, invited);
            requestRepository.save(request);
            return success;
        }
        catch (Exception e) {
            return failure;
        }
    }


}
