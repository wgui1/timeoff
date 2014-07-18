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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.util.Assert;

import cn.timeoff.security.core.CooperationNotFoundException;
import cn.timeoff.security.core.CooperationUserDetails;
import cn.timeoff.security.core.EmployeeNotFoundException;
import cn.timeoff.security.core.GroupAuthorityNotFoundException;
import cn.timeoff.security.core.GroupMemberNotFoundException;
import cn.timeoff.security.core.GroupNotFoundException;
import cn.timeoff.security.model.Authority;
import cn.timeoff.security.model.Cooperation;
import cn.timeoff.security.model.Employee;
import cn.timeoff.security.model.Group;
import cn.timeoff.security.model.GroupAuthority;
import cn.timeoff.security.model.GroupMember;
import cn.timeoff.security.model.User;
import cn.timeoff.security.repository.AuthorityRepository;
import cn.timeoff.security.repository.CooperationRepository;
import cn.timeoff.security.repository.EmployeeRepository;
import cn.timeoff.security.repository.GroupAuthorityRepository;
import cn.timeoff.security.repository.GroupMemberRepository;
import cn.timeoff.security.repository.GroupRepository;
import cn.timeoff.security.repository.UserRepository;

@Transactional
public class CooperationUserDetailsManager extends CooperationUserDetailsService
									   	   implements UserDetailsManager, CooperationGroupManager {
	
	@Autowired
	private CooperationRepository cooperationRepository; 

	@Autowired
	private UserRepository userRepository; 

	@Autowired
	private AuthorityRepository authorityRepository; 

	@Autowired
	private EmployeeRepository employeeRepository; 

	@Autowired
	private GroupRepository groupRepository; 
	
	@Autowired
	private GroupMemberRepository groupMemberRepository; 

	@Autowired
	private GroupAuthorityRepository groupAuthorityRepository; 

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
			insertUserAuthorities(dbUser, userDetails);
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
	public void createCooperation(String coName) {
		cooperationRepository.save(new Cooperation(coName));
	}

	@Override
	public void deleteCooperation(String coName) {
		Cooperation co = findCooperation(coName);
		cooperationRepository.delete(co);
	}

	@Override
	public List<String> findAllGroups(String coName) {
		return groupRepository.findNamesByCooperationName(coName);
	}

	@Override
	public List<String> findEmployeesInGroup(String coName, String groupname) {
		return groupMemberRepository.
				findMembersByCooperationNameAndGroupname(coName, groupname);
	}

	@Override
	public void createGroup(String coName, String groupname,
			List<GrantedAuthority> authorities) {
		Cooperation co = findCooperation(coName);
		Group group = new Group(co, groupname);
		final Group group1 = groupRepository.save(group);
		authorities.stream().map(p -> groupAuthorityRepository.save(
						new GroupAuthority(group1, p.getAuthority())));
	}

	@Override
	public void deleteGroup(String coName, String groupname) {
		Group group = findGroup(coName, groupname);
		groupRepository.delete(group);
	}

	@Override
	public void renameGroup(String coName, String oldName, String newName) {
		Group group = findGroup(coName, oldName);
		group.setName(newName);
		groupRepository.save(group);
	}

	@Override
	public void addEmployeeToGroup(String coName, String groupname, String username) {
		Group group = findGroup(coName, groupname);
		Employee employee = findEmployee(coName, username);
		GroupMember gm = new GroupMember(group, employee);
		groupMemberRepository.save(gm);
	}

	@Override
	public void removeEmployeeFromGroup(String coName, String groupname,
										String username) {
		GroupMember gm = findGroupMember(coName, groupname, username);
		groupMemberRepository.delete(gm);
	}

	@Override
	public List<GrantedAuthority> findGroupAuthorities(String coName,
                                                       String groupname) {
		return groupAuthorityRepository.findAuthoritiesByCooperationNameAndGroupname(
				coName, groupname).stream()
								  .map(p -> new SimpleGrantedAuthority(p))
								  .collect(Collectors.toList());
	}

	@Override
	public void addGroupAuthority(String coName, String groupname,
                                  GrantedAuthority authority) {
		Group group = findGroup(coName, groupname);
		GroupAuthority ga = new GroupAuthority(group, authority.getAuthority());
		groupAuthorityRepository.save(ga);
	}

	@Override
	public void removeGroupAuthority(String coName, String groupname,
                                     GrantedAuthority authority) {
		GroupAuthority ga = findGroupAuthority(coName, groupname, authority.getAuthority());
		groupAuthorityRepository.delete(ga);
	}

	protected Cooperation findCooperation(String cooperationName)
			throws CooperationNotFoundException {
        List<Cooperation> cooperations = cooperationRepository.findByName(cooperationName);
        if (cooperations.isEmpty()) {
            logger.debug("No resuts found for cooperation '" + cooperationName + "'");
            throw new CooperationNotFoundException(
                messages.getMessage("CooperationUserDetailsService.CooperationNotFound",
                                new Object[]{cooperationName}, "Cooperation {0} not found"));
            
        }
        Cooperation cooperation = cooperations.get(0);
        return cooperation;
    }

	protected Group findGroup(String coName, String groupname)
			throws GroupNotFoundException {
		List<Group> groups = groupRepository.
				findByCooperationNameAndGroupName(coName, groupname);
        if (groups.isEmpty()) {
            logger.debug(  "No resuts found for cooperation '" + coName + "'"
            			 + " and group '" + groupname + "'");
            throw new GroupNotFoundException(
                messages.getMessage("CooperationUserDetailsService.GroupNotFound",
                                new Object[]{groupname, coName}, "Group {0} under Cooperation {1} not found"));
            
        }
        Group group = groups.get(0);
        return group;
    }

	protected Employee findEmployee(String coName, String username)
			throws EmployeeNotFoundException {
		List<Employee> employees = employeeRepository.
				findByCooperationNameAndUsername(coName, username);
        if (employees.isEmpty()) {
            logger.debug(  "No resuts found for cooperation '" + coName + "'"
            			 + " and user '" + username + "'");
            throw new EmployeeNotFoundException(
                messages.getMessage("CooperationUserDetailsService.EmployeeNotFound",
                                new Object[]{username, coName},
                                "User {0} under Cooperation {1} not found"));
            
        }
        Employee employee = employees.get(0);
        return employee;
    }

	protected GroupAuthority findGroupAuthority(String coName, String groupname, String authority)
			throws GroupAuthorityNotFoundException {
		List<GroupAuthority> groupAuthorities = groupAuthorityRepository.
				findByCooperationNameAndGroupnameAndAuthority(coName, groupname, authority);
        if (groupAuthorities.isEmpty()) {
            logger.debug(  "No resuts found for cooperation '" + coName + "'"
            			 + " and group '" + groupname + "'"
            			 + " and authority '" + authority + "'");
            throw new GroupAuthorityNotFoundException(
                messages.getMessage("CooperationUserDetailsService.GroupAuthorityNotFound",
                                new Object[]{authority, groupname, coName},
                                "Authority {0} under Group {1} Cooperation {2} not found"));
            
        }
        GroupAuthority ga = groupAuthorities.get(0);
        return ga;
    }


	protected GroupMember findGroupMember(String coName, String groupname, String username)
			throws GroupMemberNotFoundException {
		List<GroupMember> groupMembers = groupMemberRepository.
				findByCooperationNameAndGroupnameAndUsername(coName, groupname, username);
        if (groupMembers.isEmpty()) {
            logger.debug(  "No resuts found for cooperation '" + coName + "'"
            			 + " and group '" + groupname + "'"
            			 + " and user '" + username + "'");
            throw new GroupMemberNotFoundException(
                messages.getMessage("CooperationUserDetailsService.GroupMemberNotFound",
                        new Object[]{username, groupname, coName},
                        "Employee {0} under Group {1} Cooperation {2} not found"));
            
        }
        GroupMember groupMember = groupMembers.get(0);
        return groupMember;
    }


}
