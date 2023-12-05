package onetoone.Requests;

import onetoone.Users.User;
import onetoone.Users.UserRepository;
import onetoone.websocket.ChatServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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
        } else if (userRepository.findByUsername(creator) == null) {
            return failure;
        } else if (userRepository.findByUsername(invited) == null) {
            return failure;
        }
        try {
            Request invitedRequest = new Request(parameter, creator, userRepository.findByUsername(invited));
            userRepository.findByUsername(invited).setNewRequest(invitedRequest);
            requestRepository.save(invitedRequest);
            return success;
        } catch (Exception e) {
            return failure;
        }
    }

    @PostMapping(path = "/request/accept/{id}")
    public String acceptRequest(@PathVariable int id) {
        if (requestRepository.findById(id) == null) {
            return failure;
        }
        try {
            Request newRequest = requestRepository.findById(id);
            User creator = Objects.requireNonNull(userRepository.findByUsername(newRequest.getCreator()));
            User friend = Objects.requireNonNull(userRepository.findByUsername(newRequest.getInvitedUser()));
            if (newRequest.getParameter().equals("friend")) {
                creator.addFriend(friend);
                friend.addFriend(creator);
                userRepository.save(creator);
                userRepository.save(friend);
                newRequest.setStatus(false);
                requestRepository.delete(newRequest);
                return success;
            }
        } catch (Exception e) {
            logger.info("accepting the request did not work :(");
            return "accepting the request did not work :(";
        }
        return failure;
    }

    @DeleteMapping(path = "/request/delete/{id}")
    public String deleteMapping(@PathVariable int id) {
        if (requestRepository.findById(id) == null) {
            return failure;
        }
        Request request = requestRepository.findById(id);
        request.setStatus(false);
        requestRepository.delete(request);
        return success;
    }
}
