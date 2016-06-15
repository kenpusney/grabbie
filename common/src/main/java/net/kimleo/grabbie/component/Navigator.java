package net.kimleo.grabbie.component;

public class Navigator {
    private static final String AGENT = "agent";
    private static final String EXECUTION = "execution";
    private static final String COMBINATION_FORMAT = "%s/%s";
    private static final String TASK = "task";
    private String baseUrl;

    public Navigator(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String agent() {
        return combine(baseUrl, AGENT);
    }

    public String agent(long id) {
        return combine(agent(), id);
    }

    public String agentExecutions(long id) {
        return combine(agent(id), EXECUTION);
    }

    public String execution() {
        return combine(baseUrl, EXECUTION);
    }

    public String execution(long id) {
        return combine(execution(), id);
    }

    public String task(Long id) {
        return combine(task(), id);
    }

    public String task() {
        return combine(baseUrl, TASK);
    }

    private String combine(Object agent, Object execution) {
        return String.format(COMBINATION_FORMAT, agent, execution);
    }
}
