package cn.timeoff;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@ComponentScan
@EnableAutoConfiguration
@EnableWebMvc
public class Application {

	@Bean
	public WebMvcConfigurerAdapter webConfig() {
		return new WebMvcConfigurerAdapter() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/assets/**").addResourceLocations("/assets/");
            }
		};
	}

    public static void main(String[] args) {
        @SuppressWarnings("unused")
		ConfigurableApplicationContext context = SpringApplication.run(Application.class);
    }
}