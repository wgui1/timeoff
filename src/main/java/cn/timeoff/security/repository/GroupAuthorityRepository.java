package cn.timeoff.security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import cn.timeoff.security.model.GroupAuthority;
import cn.timeoff.security.model.User;

public interface GroupAuthorityRepository extends CrudRepository<GroupAuthority, Long> {
	
	@Query( "select ga from GroupAuthority ga join ga.group g, GroupMember gm join gm.user u where gm.group = g and g.cooperation = u.cooperation and u = :user")
	public List<GroupAuthority> findByUser(@Param("user") User user);

}
