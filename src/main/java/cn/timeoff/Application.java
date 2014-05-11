package cn.timeoff;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import cn.timeoff.model.Employee;
import cn.timeoff.repository.EmployeeRepository;

@ComponentScan
@EnableAutoConfiguration
@EnableWebMvc
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class);
        EmployeeRepository repository = context.getBean(EmployeeRepository.class);

        // save a couple of customers
        repository.save(new Employee("Jack", "Bauer"));
    }
}
