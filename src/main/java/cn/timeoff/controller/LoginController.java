package cn.timeoff.controller;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.timeoff.security.core.DomainUserDetails;
import cn.timeoff.security.core.DomainUserDetailsImpl;
import cn.timeoff.security.core.CurrentUser;

@Controller
public class LoginController {

    protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private UserDetailsManager userDetailsManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String getLoginPage(Model model) {
	    return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String getLoginPage(@RequestParam("username") String username,
							   @RequestParam("password") String password,
                               Model model) {
	    return "redirect:myaccount";
	}

	@RequestMapping(value="/myaccount", method=RequestMethod.GET)
	public String userDetails(@CurrentUser DomainUserDetails userDetails, Model model) {
		model.addAttribute("user", userDetails);
		return "myaccount";
	}

	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String new_form() {
		return "register";
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String create(@RequestParam("domainName") String domainName,
	                     @RequestParam("username") String username,
						 @RequestParam("password") String password,
						 @RequestParam("email") String email,
						 Model model) {
		String encoded_pass = passwordEncoder.encode(password);
		DomainUserDetails user = new DomainUserDetailsImpl(domainName,
				username, encoded_pass, email, true, true, true, true,
				Arrays.asList(new SimpleGrantedAuthority("USER")));
		userDetailsManager.createUser(user);
		return "redirect:myaccount";
	}
}
