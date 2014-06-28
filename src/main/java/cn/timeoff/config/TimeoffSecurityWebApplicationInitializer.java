package cn.timeoff.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class TimeoffSecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { SecurityConfig.class };
    }
}
