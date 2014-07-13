package cn.timeoff.security.service;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
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
//		User dbUser = findUser(username);
//		userRepository.delete(dbUser);
		userRepository.deleteByUsername(username);
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {
		// TODO Auto-generated method stub

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
