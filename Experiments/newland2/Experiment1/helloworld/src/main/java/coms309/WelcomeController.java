package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Hello and welcome to COMS 309 This is a cool picture for you https://t.ly/SuOcD";
                //<img src="cat.jpg" height="50" width="50">;
    }

    @GetMapping("/{name}")
    public String welcome(@PathVariable String name) {
        return "Hello and welcome to COMS 309 " + name;
    }
}
