package net.kimleo.grabbie;

import net.kimleo.grabbie.model.Client;
import net.kimleo.grabbie.model.Execution;
import net.kimleo.grabbie.model.Task;
import net.kimleo.grabbie.repository.ClientRepo;
import net.kimleo.grabbie.repository.ExecRepo;
import net.kimleo.grabbie.repository.TaskRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.Date;

@SpringBootApplication
@EnableWebSecurity
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(ClientRepo clientRepo, TaskRepo taskRepo, ExecRepo execRepo) {
        return (args) -> {
            Client fuck = clientRepo.save(new Client("grabbie://fuck"));
            Client shit = clientRepo.save(new Client("grabbie://shit"));

            Task hello = taskRepo.save(new Task("echo", new String[]{"hello world"}));
            Task ps = taskRepo.save(new Task("ps", new String[]{"aux"}));

            execRepo.save(new Execution(fuck, hello));
            execRepo.save(new Execution(shit, ps));

            Execution executed = new Execution(shit, hello);
            executed.setExecuted(true);
            executed.setDate(new Date());

            execRepo.save(executed);
        };
    }

}
