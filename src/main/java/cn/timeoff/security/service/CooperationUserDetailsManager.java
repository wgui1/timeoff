package cn.timeoff.security.service;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.GroupManager;
import org.springframework.security.provisioning.UserDetailsManager;

import cn.timeoff.security.core.CooperationUserDetails;
import cn.timeoff.security.model.Cooperation;
import cn.timeoff.security.model.User;
import cn.timeoff.security.repository.UserRepository;

@Transactional
public class CooperationUserDetailsManager extends CooperationUserDetailsService implements UserDetailsManager, GroupManager {
	
	@Autowired UserRepository userRepository; 

	@Override
	public void createUser(UserDetails user){
		CooperationUserDetails coUser = (CooperationUserDetails) user;
		
		String cooperationName = coUser.getCooperation();
		Cooperation cooperation = findCooperation(cooperationName);

		User dbUser = new User(coUser.getUsername(), coUser.getEmail(), coUser.getPassword());
		dbUser.setEnabled(user.isEnabled());
		dbUser.setCooperation(cooperation);
		userRepository.save(dbUser);
	}

	@Override
	public void updateUser(UserDetails user) {
		//TODO: check if it is feasible to update password here
		CooperationUserDetails coUser = (CooperationUserDetails) user;
		User dbUser = findUser(coUser.getUsername());
		dbUser.setEnabled(coUser.isEnabled());
		dbUser.setEmail(coUser.getEmail());

		String cooperationName = coUser.getCooperation();
		if (dbUser.getCooperation().getName() != cooperationName) {
            Cooperation cooperation = findCooperation(cooperationName);
			dbUser.setCooperation(cooperation);
		}
		userRepository.save(dbUser);
	}

	@Override
	public void deleteUser(String username) {
		userRepository.deleteByUsername(username);
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();

        if (currentUser == null) {
            // This would indicate bad coding somewhere
            throw new AccessDeniedException("Can't change password as no Authentication object found in context " +
                    "for current user.");
        }

        String username = currentUser.getName();

        // If an authentication manager has been set, re-authenticate the user with the supplied password.
//        if (authenticationManager != null) {
//            logger.debug("Reauthenticating user '"+ username + "' for password change request.");
//
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));
//        } else {
//            logger.debug("No authentication manager set. Password won't be re-checked.");
//        }
//
//        logger.debug("Changing password for user '"+ username + "'");
//
//        getJdbcTemplate().update(changePasswordSql, newPassword, username);
//
//        SecurityContextHolder.getContext().setAuthentication(createNewAuthentication(currentUser, newPassword));
//
//        userCache.removeUserFromCache(username);

	}

	@Override
	public boolean userExists(String username) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> findAllGroups() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> findUsersInGroup(String groupName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createGroup(String groupName, List<GrantedAuthority> authorities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteGroup(String groupName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void renameGroup(String oldName, String newName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addUserToGroup(String username, String group) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeUserFromGroup(String username, String groupName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<GrantedAuthority> findGroupAuthorities(String groupName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addGroupAuthority(String groupName, GrantedAuthority authority) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeGroupAuthority(String groupName,
			GrantedAuthority authority) {
		// TODO Auto-generated method stub
		
	}

}
