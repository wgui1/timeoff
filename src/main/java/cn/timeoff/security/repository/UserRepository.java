package cn.timeoff.security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import cn.timeoff.security.model.User;

public interface UserRepository extends CrudRepository<User, Long>{

	@Query(   "select u from User u join u.domain d "
			+ "where d.name=:doName and u.username=:username")
	public List<User> findByDomainNameAndUsername(@Param("doName") String doName,
                                                  @Param("username") String username);

	@Query("delete User u where u.domain.name=:doName and u.username=:username")
	List<User> deleteByDomainNameAndUsername(@Param("doName") String doName,
                                             @Param("username") String username);

}
