package cn.timeoff.security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import cn.timeoff.security.model.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {

	@Query(   "select e from Employee e join e.cooperation c join e.user u "
			+ "where c.name=:coName and u.username=:username")
	public List<Employee> findByCooperationNameAndUsername(@Param("coName") String coName,
														   @Param("username") String username);
}
