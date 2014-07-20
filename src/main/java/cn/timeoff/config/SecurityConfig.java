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

import cn.timeoff.security.service.CooperationUserDetailsManager;

//@EnableWebMvcSecurity
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass=true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public CooperationUserDetailsManager userDetailsManager() {
		CooperationUserDetailsManager userManager = new CooperationUserDetailsManager();
		userManager.setRolePrefix("ROLE_");
        return userManager;
	}

	@Autowired
    private UserDetailsService userDetailsService;

    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService);
    }
    protected void configure(HttpSecurity http) throws Exception {
        http
//            .authorizeRequests()
//                .anyRequest().authenticated()
//                .and()
//                .antMatchers("/users/new").hasRole("ANONYMOUS")
//                .antMatchers(HttpMethod.POST, "/users").hasRole("ANONYMOUS")
            .anonymous()
            	.key("VVV")
            	.and()
            .formLogin()
                .loginPage("/login");
//                .permitAll();
    }
}
