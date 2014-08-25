package cn.timeoff.config;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
@EnableWebMvcSecurity
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Bean
    public DomainUserDetailsManager domainUserDetailsManager() {
        DomainUserDetailsManagerImpl userManager = new DomainUserDetailsManagerImpl();
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

    @Bean
    @Autowired
    public DomainDaoAuthenticationProvider domainDaoAuthenticationProvider(
                                    UserDetailsService userDetailsService,
                                    PasswordEncoder passwordEncoder) {
        DomainDaoAuthenticationProvider domainAuthenticationProvider =
                                         new DomainDaoAuthenticationProvider();
        domainAuthenticationProvider.setUserDetailsService(userDetailsService);
        domainAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return domainAuthenticationProvider;
    }

    @Autowired
    DomainDaoAuthenticationProvider domainAuthenticationProvider;

    @Autowired
    protected void globalConfigure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(domainAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/login", "/register").permitAll()
                .antMatchers("/cooperations/**").hasRole("EMPLOYEE")
                .antMatchers("/myaccount").hasRole("USER")
                .and()
            .anonymous()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .domainParameter("cooperation");
    }
}
