package cn.timeoff.security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import cn.timeoff.security.model.GroupAuthority;
import cn.timeoff.security.model.Employee;

public interface GroupAuthorityRepository extends CrudRepository<GroupAuthority, Long> {
	
	@Query(   "select ga from GroupAuthority ga join ga.group g, GroupMember gm join gm.employee e "
			+ "where gm.group = g and g.cooperation = e.cooperation and e = :employee")
	public List<GroupAuthority> findByEmployee(@Param("employee") Employee employee);

	@Query(   "select ga.authority from GroupAuthority ga join ga.group g join g.cooperation c "
			+ "where c.name=:coName and g.name=:groupname")
	public List<String> findAuthoritiesByCooperationNameAndGroupname(@Param("coName") String coName,
																	 @Param("groupname") String groupname);

	@Query(   "select ga from GroupAuthority ga join ga.group g join g.cooperation c "
			+ "where c.name=:coName and g.name=:groupname and ga.authority=:authority")
	public List<GroupAuthority> findByCooperationNameAndGroupnameAndAuthority(
                        @Param("coName") String coName, @Param("groupname") String groupname,
                        @Param("authority") String authority);
}
