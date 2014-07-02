package cn.timeoff.service;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import cn.timeoff.model.User;
import cn.timeoff.repository.UserRepository;

public class CooperationUserDetailsService implements UserDetailsService, MessageSourceAware {

	
	private Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private UserRepository userRepository;

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
		
		if (enableAuthorities)
		return null;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		messages = new MessageSourceAccessor(messageSource);
		
	}

}
