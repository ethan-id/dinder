package coms309;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;



class Account {
    private HashMap<String, String> Accounts = new HashMap<String, String>();


        }
@RestController
class WelcomeController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public String welcome() {

        return "This worked";
    }

    @RequestMapping(value = "/{userName}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> UserID(@PathVariable String userName) {
        if (Accounts.containsKey(userName)) {
            return new ResponseEntity<String>("Welcome" + userName, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<String>("", HttpStatus.I_AM_A_TEAPOT);
        }
    }

   @RequestMapping(value = "/{userName}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> CreateAccount(@RequestBody String userName, @RequestBody String Password) {
        Accounts.put(userName, Password);
       return new ResponseEntity<String>("Successfully created your account, " + userName, HttpStatus.OK);
   }



}
