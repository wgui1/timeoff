package cn.timeoff.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.timeoff.repository.EmployeeRepository;

@Controller
@RequestMapping("/employees")
public class EmployeeController {
	
	@Autowired
	private EmployeeRepository employee_repository;
	
	@RequestMapping("")
	public String findAll() {
		return "hello";
	}
}
