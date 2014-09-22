package cn.timeoff.repository;

import org.springframework.data.repository.CrudRepository;

import cn.timeoff.model.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {

}
