package cn.timeoff.security.core;

import org.springframework.security.core.AuthenticationException;

public class GroupMemberNotFoundException extends AuthenticationException {
    public GroupMemberNotFoundException(String msg) {
        super(msg);
    }

    public GroupMemberNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}
