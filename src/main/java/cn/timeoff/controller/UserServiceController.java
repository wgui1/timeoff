package cn.timeoff.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.timeoff.model.User;
import cn.timeoff.repository.UserRepository;

@Controller
@RequestMapping("/users")
public class UserServiceController {
	
	private final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private UserRepository user_repository;
	
	@RequestMapping("")
	public String index(Model model) {
		Iterable<User> users = user_repository.findAll();
		model.addAttribute("users", users);
		return "users/index";
	}

	@RequestMapping(value="", method=RequestMethod.POST)
	public String create(@ModelAttribute User user, Model model) {
		User user_new = user_repository.save(user);
		return "redirect:users/2";
	}

	@RequestMapping("/{user_id}")
	public String show(@PathVariable("user_id") long user_id, Model model) {
		User user = user_repository.findOne(user_id);
		log.info("user: " + user);
		model.addAttribute("user", user);
		return "users/show";
	}

	@RequestMapping("/new")
	public String new_form(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "users/new";
	}
	
}
