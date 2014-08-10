package cn.timeoff.security.core;

import org.springframework.security.core.AuthenticationException;

public class GroupAuthorityNotFoundException extends AuthenticationException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public GroupAuthorityNotFoundException(String msg) {
        super(msg);
    }

    public GroupAuthorityNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}
