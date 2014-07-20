package cn.timeoff.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/")
public class LoginController {

	@PreAuthorize("permitAll")
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String getLoginPage(
	        @RequestParam(required = false) boolean error,
	        Model model) {
	    model.addAttribute("error",
	    		error ? "You have entered an invalid username or password!" : null);
	    return "login";
	}
}
