package cn.timeoff.security.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import cn.timeoff.TimeoffVersion;

public class DomainUserDetailsImpl implements DomainUserDetails,
        CredentialsContainer {
    
    private static final long serialVersionUID = TimeoffVersion.SERIAL_VERSION_UID;

    private String password;
    private final String username;
    private String email;
    private String domainName;
    private final List<GrantedAuthority> authorities;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    public DomainUserDetailsImpl(String domainName, String username,
                                 String password, String email, boolean enabled,
                                 boolean accountNonExpired,
                                 boolean credentialsNonExpired,
                                 boolean accountNonLocked,
                                 Collection<? extends GrantedAuthority> authorities) {

        this.domainName = domainName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.authorities = Collections.unmodifiableList(sortAuthorities(authorities));
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public void eraseCredentials() {
        password = null;

    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getEmail() {
        return email;
    }

    private static List<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        for(GrantedAuthority a: authorities) {
        	if(a.getAuthority() == null) {
                throw new IllegalArgumentException("GrantedAuthority list cannot contain any null elements");
        	}
        }
        TreeSet<GrantedAuthority> authoritySet = new TreeSet<GrantedAuthority>(new Comparator<GrantedAuthority>() {
        						public int compare(GrantedAuthority o1, GrantedAuthority o2) {
        							return o1.getAuthority().compareTo(o2.getAuthority());
        						}
        });
        authoritySet.addAll(authorities);
        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.addAll(authoritySet);
        return grantedAuthorities;
    }

    @Override
    public String getDomainName() {
        return domainName;
    }

    @Override
    public void setDomainName(String domainName) {
        this.domainName = domainName;
        
    }
}
