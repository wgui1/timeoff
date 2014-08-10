package cn.timeoff.security.core;

import org.springframework.security.core.Authentication;

public interface DomainAuthentication extends Authentication {
   String getDomain ();
}
