package cn.timeoff.config;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import cn.timeoff.config.hackspring.HttpSecurity;
import cn.timeoff.config.hackspring.WebSecurityConfigurerAdapter;
import cn.timeoff.security.core.DomainDaoAuthenticationProvider;
import cn.timeoff.security.service.DomainUserDetailsManager;
import cn.timeoff.security.service.DomainUserDetailsManagerImpl;

@Configuration
@EnableWebSecurity
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/cooperations/**").hasRole("USER")
                .antMatchers("/myaccount").hasRole("USER")
                .antMatchers("/login", "/register").permitAll()
                .and()
            .anonymous()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .domainParameter("cooperation");
    }
}
