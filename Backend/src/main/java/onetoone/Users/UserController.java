package onetoone.Users;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import onetoone.Statistics.Statistic;
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

    @ApiOperation(value = "Get all users from the database", response = Iterable.class, tags = "GetAllUsers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "Not authorized!"),
            @ApiResponse(code = 403, message = "Forbidden!"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 510, message = "Internal server error")})
    @GetMapping(path = "/users")
    List<User> getAllUsers(){
        userRepository.findByUsername("l");
        return userRepository.findAll();
    }

    @ApiOperation(value = "Get a specific user by id", response = Iterable.class, tags = "GetUserById")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "Not authorized!"),
            @ApiResponse(code = 403, message = "Forbidden!"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 510, message = "Internal server error")})
    @GetMapping(path = "/users/{id}")
    User getUserById( @PathVariable int id){
        return userRepository.findById(id);
    }
    @ApiOperation(value = "Get a specific user by username", response = Iterable.class, tags = "GetUserByUsername")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "Not authorized!"),
            @ApiResponse(code = 403, message = "Forbidden!"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 510, message = "Internal server error")})
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

    @ApiOperation(value = "Create new user", response = Iterable.class, tags = "GetAllUsers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "Not authorized!"),
            @ApiResponse(code = 403, message = "Forbidden!"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 510, message = "Internal server error")})
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

    @ApiOperation(value = "Adds a new friend to the given user", response = Iterable.class, tags = "AddNewFriend")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "Not authorized!"),
            @ApiResponse(code = 403, message = "Forbidden!"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 510, message = "Internal server error")})
    @PostMapping("/friend/addFriend/{sent}")
    public String addNewFriend(@PathVariable String sent) {
        int index = sent.indexOf(",");
        String username = sent.substring(0,index);
        String friendUsername = sent.substring(index+1);
        User person = userRepository.findByUsername(username);
        User friend = userRepository.findByUsername(friendUsername);
        for (User user : person.getFriends()) {
            if (user.getUsername().equals(friendUsername)) {
                return failure;
            }
        }
        if(person!= null && friend != null && !person.getFriends().contains(friend)) {
            person.getFriends().add(friend);
            userRepository.save(person);
            return success;
        }
        return failure;
    }

    @ApiOperation(value = "Updates an existing user to match the provided user", response = Iterable.class, tags = "UpdateUser")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "Not authorized!"),
            @ApiResponse(code = 403, message = "Forbidden!"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 510, message = "Internal server error")})
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
        Statistic.totalUsers++;
        user.setVegitarian(request.isVegitarian());
        userRepository.save(user);
        return userRepository.findById(id);
    }

    @ApiOperation(value = "Deletes a user from the database", response = Iterable.class, tags = "DeleteUser")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "Not authorized!"),
            @ApiResponse(code = 403, message = "Forbidden!"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 510, message = "Internal server error")})
    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
        return success;
    }

    @ApiOperation(value = "Remove a friend by their username", response = Iterable.class, tags = "RemoveFriend")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "Not authorized!"),
            @ApiResponse(code = 403, message = "Forbidden!"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 510, message = "Internal server error")})
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

    @ApiOperation(value = "Returns all friends the user has", response = Iterable.class, tags = "GetAllFriends")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "Not authorized!"),
            @ApiResponse(code = 403, message = "Forbidden!"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 510, message = "Internal server error")})
    @GetMapping(path= "/friend/{username}/getAll")
    Set<String> getAllFriends(@PathVariable String username) {
        User user = Objects.requireNonNull(userRepository.findByUsername(username));
        if (username == null || userRepository.findByUsername(username) == null) {
            return null;
        }
        return Objects.requireNonNull(user.getAllFriends());
    }

    @ApiOperation(value = "Find a specific friend by their username", response = Iterable.class, tags = "FindFriendByUsername")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "Not authorized!"),
            @ApiResponse(code = 403, message = "Forbidden!"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 510, message = "Internal server error")})
    @GetMapping(path= "/friend/{username}/find/{friend}")
    User findFriendByUsername(@PathVariable String username, @PathVariable String friend) {
        User user = Objects.requireNonNull(userRepository.findByUsername(username));
        if (username == null || userRepository.findByUsername(username) == null || !user.getUsername().equals(username) ||
                friend == null || userRepository.findByUsername(friend) == null || !userRepository.findByUsername(friend).getUsername().equals(friend)) {
            return null;
        }
        return  Objects.requireNonNull(user.findFriendByUsername(friend));
    }

    @PostMapping(path = "/users/{username}/plus/{value}")
    String setUserDinderPlusValue(@PathVariable String username, @PathVariable boolean value) {
        if (userRepository.findByUsername(username) == null) {
            return failure;
        }
        try {
            userRepository.findByUsername(username).setPlus(value);
            userRepository.save(userRepository.findByUsername(username));
            return success;
        }
        catch (Exception e) {
            return failure;
        }
    }
    @PostMapping(path = "/admin/{username}/{value}")
    String makeMeAdmin(@PathVariable String username, @PathVariable boolean value) {
        if (userRepository.findByUsername(username) == null) {
             return failure;
        }
        if (userRepository.findByUsername(username).isAdmin()) {
            return exists;
        }
        userRepository.findByUsername(username).setAdmin(value);
        userRepository.save(userRepository.findByUsername(username));
        return success;
    }
    @GetMapping("/admin/stats")
    int getAdminStats(){
        int userCount = 4;
        int totalSwipes = 0;
        int totalLikes = 0;
        int totalMatches = 0;

        String jsonString = "{\"name\":" + "\"userCount\":" + userCount + "}";
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(jsonString);
            System.out.println(node);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 1;
    }


}

