package cn.timeoff.security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import cn.timeoff.security.model.GroupMember;

public interface GroupMemberRepository extends CrudRepository<GroupMember, Long>{
	
	@Query(   "select u.username from GroupMember gm join gm.group g join gm.user u "
			+ "join g.domain d where g.name=:groupname and d.name=:doName "
			+ "order by u.username asc")
	public List<String> findMembersByDomainNameAndGroupname(@Param("doName") String doName,
	                                                        @Param("groupname") String groupname);

	@Query(   "select gm from GroupMember gm join gm.group g join gm.user u "
			+ "join g.domain d where g.name=:groupname and d.name=:doName "
			+ "and u.username=:username")
	public List<GroupMember> findByDomainNameAndGroupnameAndUsername(
                                                        @Param("doName") String doName,
                                                        @Param("username") String username,
                                                        @Param("groupname") String groupname);

}
