package cn.timeoff.config;

import java.util.ArrayList;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import cn.timeoff.security.core.DomainDaoAuthenticationProvider;
import cn.timeoff.security.service.DomainUserDetailsManager;
import cn.timeoff.security.service.DomainUserDetailsManagerImpl;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.core.Ordered;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.annotation.Jsr250Voter;
import org.springframework.security.access.expression.method.ExpressionBasedPreInvocationAdvice;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdviceVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.util.Assert;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class CustomGlobalMethodSecurityConfiguration extends
        GlobalMethodSecurityConfiguration {
    
    private AnnotationAttributes enableMethodSecurity;

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

//    @Bean
//    @Autowired
//    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

    protected RoleHierarchyVoter roleHierarchyVoter() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER ROLE_USER > ROLE_ANONYMOUS");
        RoleHierarchyVoter roleHierarchyVoter = new RoleHierarchyVoter(roleHierarchy);
        return roleHierarchyVoter;
        
    }

    @SuppressWarnings("rawtypes")
    protected AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter> decisionVoters = new ArrayList<AccessDecisionVoter>();
        ExpressionBasedPreInvocationAdvice expressionAdvice = new ExpressionBasedPreInvocationAdvice();
        expressionAdvice.setExpressionHandler(getExpressionHandler());
        if(prePostEnabled()) {
            decisionVoters.add(new PreInvocationAuthorizationAdviceVoter(
                expressionAdvice));
        }
        if(jsr250Enabled()) {
            decisionVoters.add(new Jsr250Voter());
        }
        //decisionVoters.add(new RoleVoter());
        decisionVoters.add(roleHierarchyVoter());
        decisionVoters.add(new AuthenticatedVoter());
        return new AffirmativeBased(decisionVoters);
    }

    private boolean prePostEnabled() {
        return enableMethodSecurity().getBoolean("prePostEnabled");
    }

    private boolean jsr250Enabled() {
        return enableMethodSecurity().getBoolean("jsr250Enabled");
    }

    private AnnotationAttributes enableMethodSecurity() {
        if (enableMethodSecurity == null) {
            // if it is null look at this instance (i.e. a subclass was used)
            EnableGlobalMethodSecurity methodSecurityAnnotation = AnnotationUtils
                    .findAnnotation(getClass(),
                            EnableGlobalMethodSecurity.class);
            Assert.notNull(methodSecurityAnnotation,
                    EnableGlobalMethodSecurity.class.getName() + " is required");
            Map<String, Object> methodSecurityAttrs = AnnotationUtils
                    .getAnnotationAttributes(methodSecurityAnnotation);
            this.enableMethodSecurity = AnnotationAttributes
                    .fromMap(methodSecurityAttrs);
        }
        return this.enableMethodSecurity;
    }
}