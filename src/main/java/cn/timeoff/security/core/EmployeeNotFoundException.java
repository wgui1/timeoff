package cn.timeoff.security.core;

import org.springframework.security.core.AuthenticationException;

public class EmployeeNotFoundException extends AuthenticationException {
    public EmployeeNotFoundException(String msg) {
        super(msg);
    }

    public EmployeeNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}
