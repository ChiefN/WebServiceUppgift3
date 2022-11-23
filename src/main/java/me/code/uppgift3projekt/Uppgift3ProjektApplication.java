package me.code.uppgift3projekt;

import me.code.uppgift3projekt.data.User;
import me.code.uppgift3projekt.service.PostService;
import me.code.uppgift3projekt.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Uppgift3ProjektApplication {

    public static void main(String[] args) {
        SpringApplication.run(Uppgift3ProjektApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UserService userService, PostService postService){
        return args -> {
            User anton = userService.register("AntonAntonsson", "anton");
            User adam = userService.register("AdamAdamsson", "adam");

            postService.create(anton, "Anton1Post", "This is a sample post from Anton. Nr.1");
            postService.create(anton, "Anton2Post", "This is a sample post from Anton. Nr.2");
            postService.create(anton, "Anton3Post", "This is a sample post from Anton. Nr.3");

            postService.create(adam, "Adam1Post", "This is a sample post from Adam. Nr.1");
            postService.create(adam, "Adam2Post", "This is a sample post from Adam. Nr.2");
            postService.create(adam, "Adam3Post", "This is a sample post from Adam. Nr.3");
        };
    }

}
