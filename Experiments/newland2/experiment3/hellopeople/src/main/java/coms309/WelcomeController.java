package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple Hello World Controller to display the string returned
 *
 * @author Vivek Bengre
 */

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Try to get something";
    }

    @GetMapping("eli")
    public String eli() {
        return "Welcome Eli You have a walnut allergy";
    }

    @GetMapping("jesse")
    public String jesse() {
        return "Welcome Jesse you are a vegitarian";
        }

    @GetMapping("ethan")
    public String ethan() {
        return "Welcome Ethan you have no inputted dietary restrictions";
        }
}
