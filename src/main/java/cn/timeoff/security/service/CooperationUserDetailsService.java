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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import cn.timeoff.security.core.CooperationSecurityMessageSource;
import cn.timeoff.security.core.CooperationUserDetails;
import cn.timeoff.security.core.CooperationUserDetailsImpl;
import cn.timeoff.security.model.Authority;
import cn.timeoff.security.model.Employee;
import cn.timeoff.security.model.GroupAuthority;
import cn.timeoff.security.model.User;
import cn.timeoff.security.repository.AuthorityRepository;
import cn.timeoff.security.repository.CooperationRepository;
import cn.timeoff.security.repository.GroupAuthorityRepository;
import cn.timeoff.security.repository.UserRepository;

@Transactional
public class CooperationUserDetailsService implements UserDetailsService {

	
    protected final Log logger = LogFactory.getLog(getClass());

    private String rolePrefix = "";
    private boolean enableAuthorities = true;
    private boolean enableGroups = true;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthorityRepository authorityRepository;

	@Autowired
	private GroupAuthorityRepository groupAuthorityRepository;

	@Autowired
	private CooperationRepository cooperationRepository; 

    protected final MessageSourceAccessor messages = CooperationSecurityMessageSource.getAccessor();

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//todo: seek solution to populate cooperation in UserDetails
		User user = findUser(username);
		Employee employee = null;
		List<Employee> employees = user.getEmployees();
		
		if (user.getEmployees().size() == 1) {
			employee = employees.get(0);
		}
		
		List<GrantedAuthority> dbAuths = new ArrayList<GrantedAuthority>();
		if (enableAuthorities) {
			dbAuths.addAll(loadUserAuthorities(user));
		}
		if (enableGroups && employee != null) {
			dbAuths.addAll(loadGroupAuthorities(employee));
		}
		if (dbAuths.isEmpty()) {
			logger.debug("User '" + username + "' has no authorities, take as 'not found'");
			throw new UsernameNotFoundException(
                messages.getMessage("CooperationUserDetailsService.notFound",
                            new Object[]{username}, "User {0} has no GrantedAuthority"));
		}
        CooperationUserDetails userDetails = createUserDetails(user, dbAuths);
        userDetails.setEmployee(employee);
        return userDetails;
	}


	protected User findUser(String username) throws UsernameNotFoundException {
		List<User> users = userRepository.findByUsername(username);
		
		if (users.isEmpty()) {
			logger.debug("No resuts found for user '" + username + "'");
			throw new UsernameNotFoundException(
                messages.getMessage("CooperationUserDetailsService.notFound",
                                new Object[]{username}, "User {0} not found"));
		}
		
		User user = users.get(0);
		return user;
	}

	protected CooperationUserDetails createUserDetails(User user,
                                List<GrantedAuthority> combinedAuthorities) {
        return new CooperationUserDetailsImpl(user.getUsername(),
        		user.getPassword(), 
        		user.getEmail(),
        		user.getEnabled(),
                true, true, true, combinedAuthorities);
    }

	protected Collection<? extends GrantedAuthority> loadUserAuthorities(User user) {
		List<Authority> authorities = authorityRepository.findAllByUser(user);
		return authorities.stream()
                  .map(p -> new SimpleGrantedAuthority(rolePrefix + p.getAuthority()))
                  .collect(Collectors.toList());
	}

    protected Collection<? extends GrantedAuthority> loadGroupAuthorities(Employee employee) {
    	List<GroupAuthority> authorities = groupAuthorityRepository.findByEmployee(employee);
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

}
