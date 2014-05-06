package cn.timeoff;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import cn.timeoff.model.User;
import cn.timeoff.repository.UserRepository;

@ComponentScan
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class);
        UserRepository repository = context.getBean(UserRepository.class);

        // save a couple of customers
        repository.save(new User("Jack", "Bauer"));
    }
}
