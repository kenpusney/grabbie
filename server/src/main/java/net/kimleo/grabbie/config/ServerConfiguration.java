package net.kimleo.grabbie.config;

import net.kimleo.grabbie.component.Navigator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerConfiguration {

    @Bean
    Navigator internalNavigator() {
        return new Navigator("");
    }
}
