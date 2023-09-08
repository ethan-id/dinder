package coms309;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

/**
 * PetClinic Spring Boot Application.
 * 
 * @author Vivek Bengre
 */

@SpringBootApplication
public class Application {


	
    public static void main(String[] args) throws Exception {
        System.out.println("Enter your password");
        Scanner scnr = new Scanner(System.in);
        String input = scnr.next();
        WelcomeController.setInput(input);
        SpringApplication.run(Application.class,args);
    }



}
