package cn.timeoff.security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import cn.timeoff.security.model.Group;

public interface GroupRepository extends CrudRepository<Group, Long> {
	
	@Query(   "select g from Group g join g.cooperation c where c.name=:coName "
			+ "and g.name=:groupname")
	public List<Group> findByCooperationNameAndGroupName(@Param("coName") String coName,
														 @Param("groupname") String groupname);

	@Query("select g.name from Group g join g.cooperation c where c.name=:name order by g.name asc")
	public List<String> findNamesByCooperationName(@Param("name") String name);

}
