package onetoone.Users;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;


    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";
    private String exists = "{\"message\":\"exists\"}";

    @GetMapping(path = "/users")
    List<User> getAllUsers(){
        userRepository.findByUsername("l");
        return userRepository.findAll();
    }

    @GetMapping(path = "/users/{id}")
    User getUserById( @PathVariable int id){
        return userRepository.findById(id);
    }

    @GetMapping(path = "/users/login/{sent}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String sent){
        int index = sent.indexOf(",");
        String username = sent.substring(0,index);
        System.out.println(username);
        String passkey = sent.substring(index+1);
        User temp = userRepository.findByUsername(username);
        if(temp != null){
            if(temp.getPasskey().equals(passkey)) {
                return new ResponseEntity<>(temp, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    };


    @PostMapping(path = "/users")
    String createUser(@RequestBody User user){
        if (user == null)
            return failure;
        if(userRepository.findByUsername(user.getUsername()) != null){
            return "Already exists";
        }
        userRepository.save(user);
        return success;
    }

    @PostMapping("/addFriend/{sent}")
    public void addNewFriend(@PathVariable String sent) {
        int index = sent.indexOf(",");
        String username = sent.substring(0,index);
        String friendUsername = sent.substring(index+1);
        User person = userRepository.findByUsername(username);
        User friend = userRepository.findByUsername(friendUsername);
        for (User user : person.getFriends()) {
            if (user.getUsername().equals(friendUsername)) {
                return;
            }
        }
        if(person!= null && friend != null && !person.getFriends().contains(friend)) {
            person.getFriends().add(friend);
            userRepository.save(person);
        }
    }


    @PutMapping("/users/{id}")
    User updateUser(@PathVariable int id, @RequestBody User request){
        User user = userRepository.findById(id);
        if(user == null)
            return null;
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setPasskey(request.getPasskey());
        user.setHalal(request.isHalal());
        user.setVegan(request.isVegan());
        user.setVegitarian(request.isVegitarian());
        userRepository.save(user);
        return userRepository.findById(id);
    }


    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
        return success;
    }
    @PutMapping(path= "/friend/{username}/remove/{friendUsername}")
    String removeFriend(@PathVariable String username, @PathVariable String friendUsername) {
        User user =  Objects.requireNonNull(userRepository.findByUsername(username));
        User friend = Objects.requireNonNull(userRepository.findByUsername(friendUsername));
        if (friendUsername == null || username == null || user == null || !user.getUsername().equals(username)
                || !friend.getUsername().equals(friendUsername) || friend == null) {
            return failure;
        }
        user.removeFriend(Objects.requireNonNull(friend));
        userRepository.save(Objects.requireNonNull(user));
        return success;
    }

    @GetMapping(path= "/friend/{username}/getAll")
    Set<User> getAllFriends(@PathVariable String username) {
        User user = Objects.requireNonNull(userRepository.findByUsername(username));
        if (username == null || userRepository.findByUsername(username) == null) {
            return null;
        }
        return Objects.requireNonNull(user.getAllFriends());
    }

    @GetMapping(path= "/friend/{username}/find/{friend}")
    User findFriendByUsername(@PathVariable String username, @PathVariable String friend) {
        User user = Objects.requireNonNull(userRepository.findByUsername(username));
        if (username == null || userRepository.findByUsername(username) == null || !userRepository.findByUsername(username).getUsername().equals(username) ||
                friend == null || userRepository.findByUsername(friend) == null || !userRepository.findByUsername(friend).getUsername().equals(friend)) {
            return null;
        }
        return  Objects.requireNonNull(user.findFriendByUsername(friend));
    }


}
