package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;

@RestController
class WelcomeController {

    public static ArrayList<String> n = new ArrayList<String>();
    public static String b = "";
    public static String request = "";
    @GetMapping("/")
    public String welcome() {

        return welcome(b);
    }

    @GetMapping("/{name}")
    public String welcome(@PathVariable String name) {

        return "Informaion about "+ request + ": " + name;
    }

    public void ArrayListImport(ArrayList<String> n,String request) {
        this.n = n;
        System.out.println(n.size());
        this.request = request;
        for (int i = 0; i < n.size(); i++) {
            b += n.get(i) + '\n';
        }
    }

}
