package cn.timeoff.security.core;

import org.springframework.security.core.AuthenticationException;

public class CooperationNotFoundException extends AuthenticationException {
    public CooperationNotFoundException(String msg) {
        super(msg);
    }

    public CooperationNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}
