package cn.timeoff.config;

import org.aopalliance.intercept.MethodInterceptor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.expression.AbstractSecurityExpressionHandler;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

import cn.timeoff.config.hackspring.HttpSecurity;
import cn.timeoff.config.hackspring.WebSecurityConfigurerAdapter;
import cn.timeoff.security.core.DomainDaoAuthenticationProvider;
import cn.timeoff.security.service.DomainUserDetailsManager;
import cn.timeoff.security.service.DomainUserDetailsManagerImpl;

import org.springframework.boot.autoconfigure.security.SecurityProperties;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
	@Autowired
	private RoleHierarchy roleHierarchy;
	
    private SecurityExpressionHandler<FilterInvocation> getExpressionHandler(HttpSecurity http) {
        DefaultWebSecurityExpressionHandler defaultHandler = new DefaultWebSecurityExpressionHandler();
        AuthenticationTrustResolver trustResolver = http.getSharedObject(AuthenticationTrustResolver.class);
        if(trustResolver != null) {
            defaultHandler.setTrustResolver(trustResolver);
        }
        defaultHandler.setRoleHierarchy(roleHierarchy);
        return defaultHandler;
    }
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .withObjectPostProcessor(new SecurityPostProcessor(roleHierarchy))
                .expressionHandler(getExpressionHandler(http))
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

	private static class SecurityPostProcessor implements ObjectPostProcessor<SecurityExpressionHandler<FilterInvocation>> {
        final Log logger = LogFactory.getLog(WebSecurityConfigurerAdapter.class);
	    private RoleHierarchy roleHierarchy;

	    SecurityPostProcessor(RoleHierarchy roleHierarchy) {
	        this.roleHierarchy = roleHierarchy;
	    }
        @Override
        public <O extends SecurityExpressionHandler<FilterInvocation>> O postProcess(O object) {
                logger.debug("SecurityPostProcessor: " + object.getClass());
                if (object instanceof AbstractSecurityExpressionHandler<?>) {
                    ((AbstractSecurityExpressionHandler<FilterInvocation>) object).setRoleHierarchy(roleHierarchy);
                }
            return object;
        }
	}

}
