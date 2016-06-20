package net.kimleo.grabbie.config;

import net.kimleo.grabbie.component.Navigator;
import org.h2.server.web.WebServlet;
import org.h2.tools.Server;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
public class ServerConfiguration {

    @Bean
    Navigator internalNavigator() {
        return new Navigator("");
    }

}
