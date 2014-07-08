package cn.timeoff.security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import cn.timeoff.security.model.GroupAuthority;
import cn.timeoff.security.model.User;

public interface GroupAuthorityRepository extends CrudRepository<GroupAuthority, Long> {
	
	@Query("from GroupAuthority g where g.user = user and g.cooperation=user.cooperation and g.group")
	public List<GroupAuthority> findGroupAuthorityByUser(User user);

}
