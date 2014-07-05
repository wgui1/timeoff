package cn.timeoff.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import cn.timeoff.model.Authority;
import cn.timeoff.model.User;

public interface AuthorityRepository extends CrudRepository<Authority, Long> {
	
	public List<Authority> findAllByUser(User user);

}
