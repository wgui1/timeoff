package cn.timeoff.security.core;

import org.springframework.security.core.AuthenticationException;

public class DomainNotFoundException extends AuthenticationException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public DomainNotFoundException(String msg) {
        super(msg);
    }

    public DomainNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}
