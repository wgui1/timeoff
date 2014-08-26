package cn.timeoff;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import cn.timeoff.security.model.User;
import cn.timeoff.security.repository.UserRepository;

@ComponentScan
@EnableAutoConfiguration
@EnableWebMvc
public class Application {

    public static void main(String[] args) {
        @SuppressWarnings("unused")
		ConfigurableApplicationContext context = SpringApplication.run(Application.class);
    }
}