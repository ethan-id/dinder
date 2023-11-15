package onetoone.Requests;

import onetoone.Users.User;
import onetoone.Users.UserRepository;
import onetoone.websocket.ChatServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestController {

    public String success = "Success!";
    public String failure = "Failure";
    public String exists = "Already exists.";
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(ChatServer.class);
    @PostMapping(path = "/request/create/{creator}/{parameter}/{invited}")
    public String createNewRequest(@PathVariable String creator, @PathVariable String parameter, @PathVariable String invited) {
        if (requestRepository.findByParameter(parameter) != null) {
            return exists;
        }
        else if (userRepository.findByUsername(creator) == null) {
            return failure;
        }
        else if (userRepository.findByUsername(invited) == null) {
            return failure;
        }
        try {
            Request creatorRequest = new Request(parameter, userRepository.findByUsername(creator), "You sent a " + parameter + " request to " + invited);
            userRepository.findByUsername(creator).setNewRequest(creatorRequest);
            requestRepository.save(creatorRequest);
            Request invitedRequest = new Request(parameter, userRepository.findByUsername(invited), creator + " sent you a " + parameter + " request");
            userRepository.findByUsername(invited).setNewRequest(invitedRequest);
            requestRepository.save(invitedRequest);
            return success;
        }
        catch (Exception e) {
            return failure;
        }
    }

    @PostMapping(path = "/request/accept/{id}")
    public void acceptRequest(@PathVariable int id) {
        if (requestRepository.findById(id) == null) {
            return;
        }
        try {
           Request request = requestRepository.findById(id);
           request.setStatus(false);
           requestRepository.delete(request);
        }
        catch (Exception e) {
            System.out.println("finding or deleting the request did not work :(");
            logger.info("finding or deleting the request did not work :(");
        }
    }

}
