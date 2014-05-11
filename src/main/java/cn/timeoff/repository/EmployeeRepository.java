package cn.timeoff.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import cn.timeoff.model.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Long>{

	List<Employee> findByLastName(String lastName);
	
}
