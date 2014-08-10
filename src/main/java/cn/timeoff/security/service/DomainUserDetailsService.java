package cn.timeoff.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface DomainUserDetailsService extends UserDetailsService {

    UserDetails loadUserByDomainNameAndUsername(String domainName, String username)
                                                  throws UsernameNotFoundException;
}
