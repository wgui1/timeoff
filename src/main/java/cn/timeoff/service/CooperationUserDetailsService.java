package cn.timeoff.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import cn.timeoff.model.Authority;
import cn.timeoff.model.User;
import cn.timeoff.repository.AuthorityRepository;
import cn.timeoff.repository.UserRepository;

public class CooperationUserDetailsService implements UserDetailsService, MessageSourceAware {

	
	private Log log = LogFactory.getLog(getClass());

    private String rolePrefix = "";
    private boolean enableAuthorities = true;
    private boolean enableGroups;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthorityRepository authorityRepository;

	private MessageSourceAccessor messages;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		List<User> users = userRepository.findByUsername(username);
		
		if (users.isEmpty()) {
			log.debug("No resuts found for user '" + username + "'");
			throw new UsernameNotFoundException(
                messages.getMessage("CooperationUserDetailsService.notFound",
                                new Object[]{username}, "User {0} not found"));
		}
		
		User user = users.get(0);
		
		Set<GrantedAuthority> dbAuthsSet = new HashSet<GrantedAuthority>();
		if (enableAuthorities) {
			dbAuthsSet.addAll(loadUserAuthorities(user));
		}

        return createUserDetails(username, user, dbAuthsSet);
	}
	
	private UserDetails createUserDetails(String username, User user,
			Set<GrantedAuthority> dbAuthsSet) {
		// TODO Auto-generated method stub
		return null;
	}

	protected List<GrantedAuthority> loadUserAuthorities(User user) {
		List<Authority> authorities = authorityRepository.findAllByUser(user);
		List<GrantedAuthority> authsSet = new ArrayList<GrantedAuthority>();
		for(Authority auth: authorities) {
			authsSet.add(new SimpleGrantedAuthority(auth.getAuthority()));
		}
		return authsSet;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		messages = new MessageSourceAccessor(messageSource);
		
	}

	public boolean getEnableAuthorities() {
		return enableAuthorities;
	}

	public void setEnableAuthorities(boolean enableAuthorities) {
		this.enableAuthorities = enableAuthorities;
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

	public void setEnableGroups(boolean enableGroups) {
		this.enableGroups = enableGroups;
	}

}
