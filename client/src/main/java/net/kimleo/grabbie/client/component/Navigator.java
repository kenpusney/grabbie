package net.kimleo.grabbie.client.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Navigator {
    public static final String AGENT = "agent";
    public static final String EXECUTION = "execution";
    public static final String COMBINATION_FORMAT = "%s/%s";
    @Value("${grabbie.server.baseUrl}")
    String baseUrl;

    public String agent() {
        return combine(baseUrl, AGENT);
    }

    public String agent(long id) {
        return combine(agent(), id);
    }

    public String agentExecutions(long id) {
        return combine(agent(id), EXECUTION);
    }

    private String combine(Object agent, Object execution) {
        return String.format(COMBINATION_FORMAT, agent, execution);
    }

    public String execution() {
        return combine(baseUrl, EXECUTION);
    }

    public String execution(long id) {
        return combine(execution(), id);
    }
}
