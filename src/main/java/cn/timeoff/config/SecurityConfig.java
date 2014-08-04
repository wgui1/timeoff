package cn.timeoff.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import cn.timeoff.security.service.CooperationUserDetailsManager;
import cn.timeoff.security.service.CooperationUserDetailsService;

@Configuration
@EnableWebMvcSecurity
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public CooperationUserDetailsManager coUserDetailsManager() {
		CooperationUserDetailsManager userManager = new CooperationUserDetailsManager();
		userManager.setRolePrefix("ROLE_");
        return userManager;
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
    UserDetailsService userDetailsService;

	@Autowired
    PasswordEncoder passwordEncoder;

	@Autowired
    protected void globalConfigure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder);
    }
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/cooperations/**").hasRole("EMPLOYEE")
                .antMatchers("/myaccount").hasRole("USER")
                .antMatchers("/login", "/register").permitAll()
                .and()
            .anonymous()
            	.key("VVV")
            	.and()
            .formLogin()
                .loginPage("/login");
    }
}
