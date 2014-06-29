package cn.timeoff.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import cn.timeoff.model.Employee;
import cn.timeoff.model.User;

public interface UserRepository extends CrudRepository<User, Long>{

	List<Employee> findByLastName(String lastName);
	
}
