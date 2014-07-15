package cn.timeoff.security.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.util.Assert;

import cn.timeoff.security.core.CooperationGroupManager;
import cn.timeoff.security.core.CooperationUserDetails;
import cn.timeoff.security.model.Authority;
import cn.timeoff.security.model.Cooperation;
import cn.timeoff.security.model.Employee;
import cn.timeoff.security.model.Group;
import cn.timeoff.security.model.User;
import cn.timeoff.security.repository.AuthorityRepository;
import cn.timeoff.security.repository.EmployeeRepository;
import cn.timeoff.security.repository.GroupRepository;
import cn.timeoff.security.repository.UserRepository;

@Transactional
public class CooperationUserDetailsManager extends CooperationUserDetailsService
									   	   implements UserDetailsManager, CooperationGroupManager {
	
	@Autowired
	private UserRepository userRepository; 

	@Autowired
	private AuthorityRepository authorityRepository; 

	@Autowired
	private EmployeeRepository employeeRepository; 

	@Autowired
	private GroupRepository groupRepository; 
	
	@Autowired
	private AuthenticationManager authenticationManager;

    private UserCache userCache = new NullUserCache();

	@Override
	public void createUser(UserDetails userDetails){
		CooperationUserDetails coUserDetails = (CooperationUserDetails) userDetails;
		User dbUser = new User(coUserDetails.getUsername(), coUserDetails.getEmail(),
							   coUserDetails.getPassword());
		dbUser.setEnabled(coUserDetails.isEnabled());
		dbUser = userRepository.save(dbUser);

		if (getEnableAuthorities()) {
			authorityRepository.save(new Authority(dbUser, "USER"));
		}
		
		String cooperationName = coUserDetails.getCooperationName();
		if (cooperationName != null) {
			Cooperation cooperation = findCooperation(cooperationName);
			Employee employee = new Employee(dbUser, cooperation);
			employee = employeeRepository.save(employee);
		}
		//TODO: add group authority when it is applicable
	}

	@Override
	public void updateUser(UserDetails userDetails) {
		CooperationUserDetails coUserDetails = (CooperationUserDetails) userDetails;
		User dbUser = findUser(coUserDetails.getUsername());
		dbUser.setEnabled(coUserDetails.isEnabled());
		dbUser.setEmail(coUserDetails.getEmail());

		dbUser = userRepository.save(dbUser);

		if (getEnableAuthorities()) {
			authorityRepository.deleteByUser(dbUser);
			insertUserAuthorities(dbUser, userDetails);
		}
	}

	@Override
	public void deleteUser(String username) {
		User user = findUser(username);
		user.setEnabled(false);
		userRepository.save(user);
	}
	
	private void insertUserAuthorities(User user, UserDetails userDetails) {
		for (GrantedAuthority auth : userDetails.getAuthorities()) {
			authorityRepository.save(new Authority(user, auth.getAuthority()));
		}
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();

        if (currentUser == null) {
            // This would indicate bad coding somewhere
            throw new AccessDeniedException(
            		messages.getMessage("CooperationUserDetailsService.AccessDenied",
                      "Can't change password as no Authentication object found in context "
            		+ "for current user."));
        }

        String username = currentUser.getName();
        User user = findUser(username);

        // If an authentication manager has been set, re-authenticate the user with the supplied password.
        if (authenticationManager != null) {
            logger.debug("Reauthenticating user '"+ username + "' for password change request.");

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));
        } else {
            logger.debug("No authentication manager set. Password won't be re-checked.");
        }

        logger.debug("Changing password for user '"+ username + "'");

        user.setPassword(newPassword);
        userRepository.save(user);

        SecurityContextHolder.getContext().setAuthentication(createNewAuthentication(currentUser, newPassword));

        userCache.removeUserFromCache(username);

	}

    protected Authentication createNewAuthentication(Authentication currentAuth, String newPassword) {
        UserDetails user = loadUserByUsername(currentAuth.getName());

        UsernamePasswordAuthenticationToken newAuthentication =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        newAuthentication.setDetails(currentAuth.getDetails());

        return newAuthentication;
    }

	@Override
	public boolean userExists(String username) {
		List<User> users = userRepository.findByUsername(username);
		return !users.isEmpty();
	}

    /**
     * Optionally sets the UserCache if one is in use in the application.
     * This allows the user to be removed from the cache after updates have taken place to avoid stale data.
     *
     * @param userCache the cache used by the AuthenticationManager.
     */
    public void setUserCache(UserCache userCache) {
        Assert.notNull(userCache, "userCache cannot be null");
        this.userCache = userCache;
    }

	@Override
	public List<String> findAllGroups(String coName) {
		List<Group> groups = groupRepository.findByCooperationName(coName);
		return null;
	}

	@Override
	public List<String> findUsersInGroup(String coName, String groupName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createGroup(String coName, String groupName,
			List<GrantedAuthority> authorities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteGroup(String coName, String groupName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void renameGroup(String coName, String oldName, String newName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addUserToGroup(String coName, String username, String group) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeUserFromGroup(String coName, String username,
			String groupName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<GrantedAuthority> findGroupAuthorities(String coName,
			String groupName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addGroupAuthority(String coName, String groupName,
			GrantedAuthority authority) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeGroupAuthority(String coName, String groupName,
			GrantedAuthority authority) {
		// TODO Auto-generated method stub
		
	}

}
