package net.kimleo.grabbie.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GrabbieClient {
    public static void main(String[] args) {
        SpringApplication.run(GrabbieClient.class, args);
    }
}
