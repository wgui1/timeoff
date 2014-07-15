package cn.timeoff.security.core;

import org.springframework.security.core.AuthenticationException;

public class GroupNotFoundException extends AuthenticationException {
    public GroupNotFoundException(String msg) {
        super(msg);
    }

    public GroupNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}
