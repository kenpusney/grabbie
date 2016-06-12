package net.kimleo.grabbie.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;

@Configuration
public class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {

    @Value("${grabbie.admin.username}")
    private String adminUsername;

    @Value("${grabbie.admin.password}")
    private String adminPassword;

    @Value("${grabbie.app.username}")
    private String appUsername;

    @Value("${grabbie.app.password}")
    private String appPassword;
    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(adminUsername).password(adminPassword).roles("ADMIN", "USER")
                .and()
                .withUser(appUsername).password(appPassword).roles("USER");
    }
}
