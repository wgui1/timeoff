package cn.timeoff.security.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import cn.timeoff.security.model.User;

public interface UserRepository extends CrudRepository<User, Long>{

	List<User> findByUsername(String username);

	List<User> deleteByUsername(String username);

}
