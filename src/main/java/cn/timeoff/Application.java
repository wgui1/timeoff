package cn.timeoff;

import java.io.File;
import java.io.IOException;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
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
                registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/assets/");
            }
		};
	}
	
    @ConditionalOnWebApplication
	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
	    TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
	    tomcat.addAdditionalTomcatConnectors(createSslConnector());
	    return tomcat;
	}

	private Connector createSslConnector() {
	    Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
	    Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
	    try {
	        File keystore = new ClassPathResource("keystore").getFile();
	        File truststore = new ClassPathResource("keystore").getFile();
	        connector.setScheme("https");
	        connector.setSecure(true);
	        connector.setPort(8443);
	        protocol.setSSLEnabled(true);
	        protocol.setKeystoreFile(keystore.getAbsolutePath());
	        protocol.setKeystorePass("Mogen12#");
	        protocol.setTruststoreFile(truststore.getAbsolutePath());
	        protocol.setTruststorePass("Mogen12#");
	        protocol.setKeyAlias("tomcat");
	        return connector;
	    }
	    catch (IOException ex) {
	        throw new IllegalStateException("can't access keystore: [" + "keystore"
	                + "] or truststore: [" + "keystore" + "]", ex);
	    }
	}

    public static void main(String[] args) {
        @SuppressWarnings("unused")
		ConfigurableApplicationContext context = SpringApplication.run(Application.class);
    }
}