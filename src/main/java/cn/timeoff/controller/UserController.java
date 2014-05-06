package cn.timeoff.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.timeoff.repository.UserRepository;

@Controller
@RequestMapping("/")
public class UserController {
	
	@Autowired
	private UserRepository user_repository;
	
	@RequestMapping("users")
	public String findAll() {
		return "hello.html";
	}
}
