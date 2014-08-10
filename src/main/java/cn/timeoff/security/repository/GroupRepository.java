package cn.timeoff.security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import cn.timeoff.security.model.Group;

public interface GroupRepository extends CrudRepository<Group, Long> {
	
	@Query(   "select g from Group g join g.domain d where d.name=:doName "
			+ "and g.name=:groupname")
	public List<Group> findByDomainNameAndGroupName(@Param("doName") String doName,
                                                    @Param("groupname") String groupname);

	@Query("select g.name from Group g join g.domain d where d.name=:name order by g.name asc")
	public List<String> findNamesByDomainName(@Param("name") String name);

}
