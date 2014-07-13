package cn.timeoff.security.core;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import cn.timeoff.TimeoffVersion;
import cn.timeoff.security.model.Employee;

public class CooperationUserDetailsImpl implements CooperationUserDetails,
		CredentialsContainer {
	
	private static final long serialVersionUID = TimeoffVersion.SERIAL_VERSION_UID;

	private String password;
    private final String username;
    private String email;
    private Employee employee;
    private final List<GrantedAuthority> authorities;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    public CooperationUserDetailsImpl(String username, String password,
    							  String email, boolean enabled, boolean accountNonExpired,
    							  boolean credentialsNonExpired,
                                  boolean accountNonLocked,
                                  Collection<? extends GrantedAuthority> authorities) {

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

	@Override
	public Employee getEmployee() {
		return employee;
	}
	
	@Override
	public boolean isEmployee() {
		return employee != null;
	}

	@Override
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	private static List<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        if (authorities.stream().anyMatch(g -> g.getAuthority() == null)) {
        		throw new IllegalArgumentException("GrantedAuthority list cannot contain any null elements");
        }
        List<GrantedAuthority> asList = authorities.stream().distinct()
                           .sorted( (g1, g2) -> g1.getAuthority().compareTo(g2.getAuthority()) )
                           .collect(Collectors.toList());
        return asList;
    }
}
