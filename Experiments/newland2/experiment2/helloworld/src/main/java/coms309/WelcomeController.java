package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
class WelcomeController {
    static String pass = "";
    static String passkey = "1234";
    @GetMapping("/")
    public String welcome() {
        if(pass.equals(passkey)){
         return "Correct";
        }
        else {
            return"Incorrect";
        }

    }
    public static void setInput(String input){
        pass = input;
    }

    @GetMapping("/{name}")
    public String welcome(@PathVariable String name) {
        return "aldkfj " + name;
    }
}
