package cn.timeoff.security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import cn.timeoff.security.model.GroupAuthority;
import cn.timeoff.security.model.User;

public interface GroupAuthorityRepository extends CrudRepository<GroupAuthority, Long> {
	
	//@Query( "select ga from GroupAuthority ga, Group g, GroupMember gm, User u where g.cooperation = u.cooperation and gm.group = g and gm.user = u and ga.group = g and u = :user")
	//@Query("select ga from GroupAuthority ga join ga.group ")
	public List<GroupAuthority> findByUser(User user);

}
