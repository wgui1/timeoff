package cn.timeoff.security.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

import cn.timeoff.security.core.CooperationNotFoundException;
import cn.timeoff.security.core.CooperationUserDetails;
import cn.timeoff.security.core.CooperationUserDetailsImpl;
import cn.timeoff.security.model.Authority;
import cn.timeoff.security.model.Cooperation;
import cn.timeoff.security.model.GroupAuthority;
import cn.timeoff.security.model.User;
import cn.timeoff.security.repository.AuthorityRepository;
import cn.timeoff.security.repository.CooperationRepository;
import cn.timeoff.security.repository.GroupAuthorityRepository;
import cn.timeoff.security.repository.UserRepository;

public class CooperationUserDetailsService implements UserDetailsService, MessageSourceAware {

	
	protected Log log = LogFactory.getLog(getClass());

    private String rolePrefix = "";
    private boolean enableGroups = true;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthorityRepository authorityRepository;

	@Autowired
	private GroupAuthorityRepository groupAuthorityRepository;

	@Autowired CooperationRepository cooperationRepository; 

	protected MessageSourceAccessor messages;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		User user = findUser(username);
		
		List<GrantedAuthority> dbAuths = new ArrayList<GrantedAuthority>();
		if (enableGroups) {
			dbAuths.addAll(loadGroupAuthorities(user));
		}
		if (dbAuths.isEmpty()) {
			log.debug("User '" + username + "' has no authorities, take as 'not found'");
			throw new UsernameNotFoundException(
                messages.getMessage("CooperationUserDetailsService.notFound",
                            new Object[]{username}, "User {0} has no GrantedAuthority"));
		}
        return createUserDetails(user, dbAuths);
	}


	protected User findUser(String username) throws UsernameNotFoundException {
		List<User> users = userRepository.findByUsername(username);
		
		if (users.isEmpty()) {
			log.debug("No resuts found for user '" + username + "'");
			throw new UsernameNotFoundException(
                messages.getMessage("CooperationUserDetailsService.notFound",
                                new Object[]{username}, "User {0} not found"));
		}
		
		User user = users.get(0);
		return user;
	}

	protected Cooperation findCooperation(String cooperationName)
			throws CooperationNotFoundException {
        List<Cooperation> cooperations = cooperationRepository.findByName(cooperationName);
        if (cooperations.isEmpty()) {
            log.debug("No resuts found for cooperation '" + cooperationName + "'");
            throw new CooperationNotFoundException(
                messages.getMessage("CooperationUserDetailsService.CooperationNotFound",
                                new Object[]{cooperationName}, "Cooperation {0} not found"));
            
        }
        Cooperation cooperation = cooperations.get(0);
        return cooperation;
    }

	protected CooperationUserDetails createUserDetails(User user,
                                List<GrantedAuthority> combinedAuthorities) {
        return new CooperationUserDetailsImpl(user.getUsername(),
        		user.getCooperation().getName(),
        		user.getEmail(),
        		user.getPassword(), 
        		user.getEnabled(),
                true, true, true, combinedAuthorities);
    }

	protected Collection<? extends GrantedAuthority> loadUserAuthorities(User user) {
		List<Authority> authorities = authorityRepository.findAllByUser(user);
		return authorities.stream()
                  .map(p -> new SimpleGrantedAuthority(rolePrefix + p.getAuthority()))
                  .collect(Collectors.toList());
	}

    protected Collection<? extends GrantedAuthority> loadGroupAuthorities(
			User user) {
    	List<GroupAuthority> authorities = groupAuthorityRepository.findByUser(user);
		return authorities.stream()
                  .map(p -> new SimpleGrantedAuthority(rolePrefix + p.getAuthority()))
                  .collect(Collectors.toList());
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		messages = new MessageSourceAccessor(messageSource);
		
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
	
	public boolean getEnableGroups() {
		return enableGroups;
	}

}
