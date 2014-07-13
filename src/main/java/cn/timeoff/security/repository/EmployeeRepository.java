package cn.timeoff.security.repository;

import org.springframework.data.repository.CrudRepository;

import cn.timeoff.security.model.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {

}
