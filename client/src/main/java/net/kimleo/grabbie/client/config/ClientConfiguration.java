package net.kimleo.grabbie.client.config;

import net.kimleo.grabbie.client.agent.AgentInfo;
import net.kimleo.grabbie.client.component.IgnoreHttpStatusHandler;
import net.kimleo.grabbie.component.Navigator;
import net.kimleo.grabbie.model.Agent;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.function.Function;

@Configuration
public class ClientConfiguration {

    @Value("${grabbie.agent.id}")
    String clientId;


    @Bean
    Navigator navigator(@Value("${grabbie.server.baseUrl}") String baseUrl) {
        return new Navigator(baseUrl);
    }

    @Bean
    RestTemplate restTemplate(@Value("${grabbie.app.username}") String username,
                              @Value("${grabbie.app.password}") String password) {
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(username, password));
        return new RestTemplate(
                new HttpComponentsClientHttpRequestFactory(
                        HttpClientBuilder.create().
                                setDefaultCredentialsProvider(credentialsProvider)
                                .build()));
    }

    @Bean
    AgentInfo retrieveAgentInfo(RestTemplate restTemplate, Navigator navigator) {
        RequestEntity<Agent> agent = RequestEntity.post(URI.create(navigator.agent()))
                .body(new Agent(clientId));
        ResponseEntity<Agent> respondAgent =
                withErrorHandler(restTemplate,
                        (template) ->
                                template.exchange(navigator.agent(),
                                        HttpMethod.POST, agent, Agent.class));

        return new AgentInfo(respondAgent.getBody());
    }

    private <T> T withErrorHandler(RestTemplate template,
                                   Function<RestTemplate, T> processor) {
        ResponseErrorHandler originalHandler = template.getErrorHandler();
        IgnoreHttpStatusHandler newHandler = new IgnoreHttpStatusHandler(originalHandler, HttpStatus.CONFLICT);

        template.setErrorHandler(newHandler);
        T result = processor.apply(template);
        template.setErrorHandler(originalHandler);
        return result;
    }

}
