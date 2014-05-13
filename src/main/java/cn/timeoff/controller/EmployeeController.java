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

import cn.timeoff.model.Employee;
import cn.timeoff.repository.EmployeeRepository;

@Controller
@RequestMapping("/employees")
public class EmployeeController {
	
	private final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private EmployeeRepository employee_repository;
	
	@RequestMapping("")
	public String index(Model model) {
		Iterable<Employee> employees = employee_repository.findAll();
		model.addAttribute("employees", employees);
		return "employees/index";
	}

	@RequestMapping(value="", method=RequestMethod.POST)
	public String create(@ModelAttribute Employee employee, Model model) {
		Employee employee_new = employee_repository.save(employee);
		return "redirect:employees/2";
	}

	@RequestMapping("/{employee_id}")
	public String show(@PathVariable("employee_id") long employee_id, Model model) {
		Employee employee = employee_repository.findOne(employee_id);
		log.info("employee: " + employee);
		model.addAttribute("employee", employee);
		return "employees/show";
	}

	@RequestMapping("/new")
	public String new_form(Model model) {
		Employee employee = new Employee();
		model.addAttribute("employee", employee);
		return "employees/new";
	}
	
}
