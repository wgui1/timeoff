package cn.timeoff.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.timeoff.security.core.CooperationUserDetails;

@Controller
@RequestMapping("/users")
public class UserServiceController {
	
	private final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private UserDetailsManager userDetailsManager;
	

	@PreAuthorize("isAnonymous()")
	@RequestMapping(value="", method=RequestMethod.POST)
	public String create(@ModelAttribute CooperationUserDetails user, Model model) {
		userDetailsManager.createUser(user);
		return "redirect:users";
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping("/{user_id}")
	public String show(@PathVariable("user_id") long user_id, Model model) {
//		User user = user_repository.findOne(user_id);
//		log.info("user: " + user);
//		model.addAttribute("user", user);
		return "users/show";
	}

	@PreAuthorize("isAnonymous()")
	@RequestMapping("/new")
	public String new_form(Model model) {
		return "users/new";
	}
	
}
