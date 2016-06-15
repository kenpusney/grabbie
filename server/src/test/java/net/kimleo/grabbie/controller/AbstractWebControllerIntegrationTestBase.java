package net.kimleo.grabbie.controller;

import net.kimleo.grabbie.GrabbieServer;
import net.kimleo.grabbie.component.Navigator;
import net.kimleo.grabbie.repository.AgentRepo;
import net.kimleo.grabbie.repository.ExecRepo;
import net.kimleo.grabbie.repository.TaskRepo;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringApplicationConfiguration(GrabbieServer.class)
@WebAppConfiguration
public class AbstractWebControllerIntegrationTestBase {
    protected MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
    protected MockMvc mockMvc;
    @Autowired
    @Qualifier("internalNavigator")
    Navigator navigator;
    @Value("${grabbie.app.username}")
    String user;
    @Value("${grabbie.app.password}")
    String pass;
    @Autowired
    AgentRepo agentRepo;
    @Autowired
    TaskRepo taskRepo;
    @Autowired
    ExecRepo execRepo;RequestPostProcessor auth;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();
    }

    @Before
    public void setup() throws Exception {
        execRepo.deleteAll();
        agentRepo.deleteAll();
        taskRepo.deleteAll();
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        auth = httpBasic(user, pass);
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
