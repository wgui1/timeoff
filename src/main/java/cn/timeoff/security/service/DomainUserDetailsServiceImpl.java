package cn.timeoff.security.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import cn.timeoff.security.core.DomainSecurityMessageSource;
import cn.timeoff.security.core.DomainUserDetails;
import cn.timeoff.security.core.DomainUserDetailsImpl;
import cn.timeoff.security.model.Authority;
import cn.timeoff.security.model.GroupAuthority;
import cn.timeoff.security.model.User;
import cn.timeoff.security.repository.AuthorityRepository;
import cn.timeoff.security.repository.DomainRepository;
import cn.timeoff.security.repository.GroupAuthorityRepository;
import cn.timeoff.security.repository.UserRepository;

@Transactional
public class DomainUserDetailsServiceImpl implements DomainUserDetailsService {

    
    protected final Log logger = LogFactory.getLog(getClass());

    private String rolePrefix = "";
    protected String defaultDomainName = "";
    protected boolean enableDefaultDomainQuery = false;
    private boolean enableAuthorities = true;
    private boolean enableGroups = true;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private GroupAuthorityRepository groupAuthorityRepository;

    @Autowired
    private DomainRepository domainRepository; 

    protected final MessageSourceAccessor messages = DomainSecurityMessageSource.getAccessor();

    @Override
    public UserDetails loadUserByDomainNameAndUsername(String domainName, String username)
                                                        throws UsernameNotFoundException {
        User user = findUser(domainName, username);
        
        List<GrantedAuthority> dbAuths = new ArrayList<GrantedAuthority>();
        if (enableAuthorities) {
            dbAuths.addAll(loadUserAuthorities(user));
        }
        if (enableGroups) {
            dbAuths.addAll(loadGroupAuthorities(user));
        }
        if (dbAuths.isEmpty()) {
            logger.debug("User '" + username + "' has no authorities, take as 'not found'");
            throw new UsernameNotFoundException(
                messages.getMessage("DomainUserDetailsService.AuthorityNotFound",
                            new Object[]{username, domainName},
                            "User {0} under Domain {1} has no GrantedAuthority"));
        }
        DomainUserDetails userDetails = createUserDetails(domainName, username,
                user.getPassword(), user.getEmail(), user.getEnabled(), dbAuths);
        return userDetails;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        UserDetails rc = null;
        if (enableDefaultDomainQuery) {
            rc = loadUserByDomainNameAndUsername(defaultDomainName, username);
        }
        return rc;
    }

    protected User findUser(String domainName, String username)
                                throws UsernameNotFoundException {
        List<User> users = userRepository.findByDomainNameAndUsername(domainName,
                                                                      username);
        
        if (users.isEmpty()) {
            logger.debug("No resuts found for user '" + username + "'"
                         + " '" + domainName + "'");
            throw new UsernameNotFoundException(
                messages.getMessage("DomainUserDetailsService.notFound",
                                new Object[]{username, domainName},
                                "User {0} under domain {1} not found"));
        }
        
        User user = users.get(0);
        return user;
    }

    protected DomainUserDetails createUserDetails(String domainName,
            String username, String password, String email,
            boolean isEnabled, List<GrantedAuthority> combinedAuthorities) {
        return new DomainUserDetailsImpl(domainName, username, password, email,
                isEnabled, true, true, true, combinedAuthorities);
    }

    protected Collection<? extends GrantedAuthority> loadUserAuthorities(User user) {
        List<Authority> authorities = authorityRepository.findAllByUser(user);
        return authorities.stream()
                  .map(p -> new SimpleGrantedAuthority(rolePrefix + p.getAuthority()))
                  .collect(Collectors.toList());
    }

    protected Collection<? extends GrantedAuthority> loadGroupAuthorities(User user) {
        List<GroupAuthority> authorities = groupAuthorityRepository.findByUser(user);
        return authorities.stream()
                  .map(p -> new SimpleGrantedAuthority(rolePrefix + p.getAuthority()))
                  .collect(Collectors.toList());
    }

    public String getRolePrefix() {
        return rolePrefix;
    }

    public void setRolePrefix(String rolePrefix) {
        this.rolePrefix = rolePrefix;
    }

    public boolean isEnableGroups() {
        return enableGroups;
    }

    public boolean getEnableGroups() {
        return enableGroups;
    }

    public void setEnableGroups(boolean enableGroups) {
        this.enableGroups = enableGroups;
    }
    
    public boolean isEnableAuthorities() {
        return enableAuthorities;
    }

    public boolean getEnableAuthorities() {
        return enableAuthorities;
    }

    public void setEnableAuthorities(boolean enableAuthorities) {
        this.enableAuthorities = enableAuthorities;
    }

    public boolean isEnableDefaultDomainQuery() {
        return enableDefaultDomainQuery;
    }

    public void setEnableDefaultDomainQuery(boolean enableDefaultDomainQuery) {
        this.enableDefaultDomainQuery = enableDefaultDomainQuery;
    }

}
