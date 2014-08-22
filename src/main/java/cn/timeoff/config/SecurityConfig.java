package cn.timeoff.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import cn.timeoff.security.core.DomainDaoAuthenticationProvider;
import cn.timeoff.security.core.DomainUsernamePasswordAuthenticationFilter;
import cn.timeoff.security.service.DomainUserDetailsManager;
import cn.timeoff.security.service.DomainUserDetailsManagerImpl;

@Configuration
@EnableWebMvcSecurity
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
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

    @SuppressWarnings("deprecation")
    @Bean
    @Autowired
    public DomainUsernamePasswordAuthenticationFilter domainUsernamePasswordAuthenticationFilter(
                                        AuthenticationConfiguration authenticationConfiguration) throws Exception {
        DomainUsernamePasswordAuthenticationFilter filter = new DomainUsernamePasswordAuthenticationFilter();
        filter.setAuthenticationManager(authenticationConfiguration.getAuthenticationManager());
        filter.setFilterProcessesUrl("/login");
        filter.setDomainParameter("cooperation");
        return filter;
    }

    @Autowired
    DomainDaoAuthenticationProvider domainAuthenticationProvider;

    @Autowired 
    DomainUsernamePasswordAuthenticationFilter domainUserPasswordAuthenticationFilter;

    @Autowired
    protected void globalConfigure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(domainAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/cooperations/**").hasRole("EMPLOYEE")
                .antMatchers("/myaccount").hasRole("USER")
                .antMatchers("/login", "/register").permitAll();
        http.addFilterBefore(domainUserPasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
