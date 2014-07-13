package cn.timeoff.security.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import cn.timeoff.security.model.Authority;
import cn.timeoff.security.model.User;

public interface AuthorityRepository extends CrudRepository<Authority, Long> {
	
	public List<Authority> findAllByUser(User user);

	public void deleteByUser(User user);

}
