package onetoone.Users;

import java.util.List;

import onetoone.Likes.Liked;
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
    @PostMapping(path = "/users/{username}/{userid}/{passkey}")
    String createANewUser(@PathVariable String username, @PathVariable String userid, @PathVariable String passkey){
        if(userRepository.findByUsername(username) != null){
            return "Already exists";
        }
        userRepository.save(new User(username, userid, passkey));
        return success;
    }

    @PostMapping("/addFriend/{sent}")
    public void addNewFriend(@PathVariable String sent) {
        int index = sent.indexOf(",");
        String username = sent.substring(0,index);
        System.out.println(username);
        String friendUsername = sent.substring(index+1);
        User person = userRepository.findByUsername(username);
        User friend = userRepository.findByUsername(friendUsername);
        if(person!= null && friend != null) {
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

    @GetMapping(path= "/users/add-friend/{username}")
    String addFriend(@PathVariable String username) {
        if (username == null || userRepository.findByUsername(username) == null) {
            return failure;
        }

        return success;
    }

    @PutMapping(path = "/users/favorites/{username}")
    String deleteUserFavorite(@PathVariable String username){
        User user = userRepository.findByUsername(username);
        user.deleteFavorites();
        userRepository.save(user);
        return success;
    }

}
