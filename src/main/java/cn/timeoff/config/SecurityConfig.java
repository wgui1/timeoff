package cn.timeoff.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

import cn.timeoff.security.service.CooperationUserDetailsManager;

@Configuration
@EnableWebMvcSecurity
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public UserDetailsService userDetailService() {
		CooperationUserDetailsManager userManager = new CooperationUserDetailsManager();
		userManager.setRolePrefix("ROLE_");
        return (UserDetailsService) userManager;
	}
	
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService);

        auth
        	.inMemoryAuthentication()
            .withUser("user").password("password").roles("USER");
    }
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .anyRequest().anonymous()
            .and()
            .formLogin()
            .and()
            .httpBasic();
    }
}
