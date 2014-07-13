package cn.timeoff.config;

import javax.annotation.Resource;

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
import cn.timeoff.security.service.CooperationUserDetailsService;

@Configuration
@EnableWebMvcSecurity
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public CooperationUserDetailsManager userDetailsManager() {
		CooperationUserDetailsManager userManager = new CooperationUserDetailsManager();
		userManager.setRolePrefix("ROLE_");
        return userManager;
	}

    private UserDetailsService userDetailsService;

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
