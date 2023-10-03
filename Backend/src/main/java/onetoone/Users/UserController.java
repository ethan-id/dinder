package onetoone.Users;

import java.util.List;

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


    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";
    private String exists = "{\"message\":\"exists\"}";

    @GetMapping(path = "/users")
    List<User> getAllUsers(){
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
}
