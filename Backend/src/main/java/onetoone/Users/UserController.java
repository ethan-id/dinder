package onetoone.Users;

import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;




@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

//    private static FriendRepository friendRepository;
//    @Autowired
//    public void setFriendRepository(FriendRepository friendRepository) {
//        this.friendRepository = friendRepository;
//    }
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

    @GetMapping(path= "/users/{username}/add-friend/{friendUsername}")
    String addFriend(@PathVariable String username, @PathVariable String friendUsername) {
        User user =  userRepository.findByUsername(username);
        User friend =  userRepository.findByUsername(friendUsername);
        if (friendUsername == null || friend == null || username == null || user == null) {
            return failure;
        }
//        friendRepository.save(friend);
        userRepository.save(user);
        user.addFriend(friend);
        return success;
    }

    @GetMapping(path= "/users/{username}/remove-friend/{friendUsername}")
    String removeFriend(@PathVariable String username, @PathVariable String friendUsername) {
        User user =  userRepository.findByUsername(username);
        User friend =  userRepository.findByUsername(friendUsername);
        if (friendUsername == null || friend == null || username == null || user == null) {
            return failure;
        }
        user.removeFriend(friendUsername);
        userRepository.delete(user);
//        friendRepository.delete(friend);
        return success;
    }

    @GetMapping(path= "/users/{username}/getFriends/")
    Set<User> findAllFriends(@PathVariable String username) {
        if (username == null || userRepository.findByUsername(username) == null) {
            return null;
        }
        return userRepository.findByUsername(username).getFriends();
    }

    @GetMapping(path= "/users/{username}/find-friend/{friendUsername}/")
    User findAllFriends(@PathVariable String username, @PathVariable String friendUsername) {
        if (username == null || userRepository.findByUsername(username) == null) {
            return null;
        }
        return userRepository.findByUsername(username).findFriendByUsername(friendUsername);
    }

}
