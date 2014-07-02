package cn.timeoff.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MessageConfig {

	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
		ms.setBasename("cn.timeoff.messages");
		return ms;
	}

}
