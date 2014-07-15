package cn.timeoff.security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import cn.timeoff.security.model.GroupMember;

public interface GroupMemberRepository extends CrudRepository<GroupMember, Long>{
	
	@Query(   "select u.username from GroupMember gm join gm.group g join gm.employee e "
			+ "join e.user u join g.cooperation c where g.name=:groupname and c.name=:coName "
			+ "order by u.username asc")
	public List<String> findMembersByCooperationNameAndGroupname(@Param("coName") String coName,
													  			 @Param("groupname") String groupname);

	@Query(   "select gm from GroupMember gm join gm.group g join gm.employee e "
			+ "join e.user u join g.cooperation c where g.name=:groupname and c.name=:coName "
			+ "and u.username=:username")
	public List<GroupMember> findByCooperationNameAndGroupnameAndUsername(
														@Param("coName") String coName,
														@Param("username") String username,
                                                        @Param("groupname") String groupname);

}
