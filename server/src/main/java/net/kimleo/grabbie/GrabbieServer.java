package net.kimleo.grabbie;

import net.kimleo.grabbie.model.Agent;
import net.kimleo.grabbie.model.Execution;
import net.kimleo.grabbie.model.Task;
import net.kimleo.grabbie.model.summary.TaskSummary;
import net.kimleo.grabbie.repository.AgentRepo;
import net.kimleo.grabbie.repository.ExecRepo;
import net.kimleo.grabbie.repository.TaskRepo;
import net.kimleo.grabbie.repository.TaskSummaryRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.Date;

@SpringBootApplication
@EnableWebSecurity
public class GrabbieServer {

    public static void main(String[] args) {
        SpringApplication.run(GrabbieServer.class, args);
    }

    @Bean
    public CommandLineRunner demo(AgentRepo agentRepo,
                                  TaskRepo taskRepo,
                                  ExecRepo execRepo,
                                  TaskSummaryRepo taskSummaries) {
        return (args) -> {
            Agent fuck = agentRepo.save(new Agent("grabbie://fuck"));
            Agent shit = agentRepo.save(new Agent("grabbie://shit"));

            Task hello = taskRepo.save(new Task("echo", new String[]{"hello world"}));
            Task ps = taskRepo.save(new Task("ps", new String[]{"aux"}));

            taskSummaries.save(new TaskSummary(hello));
            taskSummaries.save(new TaskSummary(ps));

            execRepo.save(new Execution(fuck, hello));
            execRepo.save(new Execution(shit, ps));

            Execution executed = new Execution(shit, hello);
            executed.setExecuted(true);
            executed.setDate(new Date());

            execRepo.save(executed);
        };
    }

}
