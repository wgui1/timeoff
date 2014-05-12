package cn.timeoff.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.timeoff.model.Employee;
import cn.timeoff.repository.EmployeeRepository;

@Controller
@RequestMapping("/employees")
public class EmployeeController {
	
	private final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private EmployeeRepository employee_repository;
	
	@RequestMapping("/{employee_id}")
	public String find(@PathVariable("employee_id") long employee_id, Model model) {
		Employee employee = employee_repository.findOne(employee_id);
		log.info("employee: " + employee);
		model.addAttribute("employee", employee);
		return "employees/edit";
	}
}
