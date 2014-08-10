package cn.timeoff.security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import cn.timeoff.security.model.GroupAuthority;
import cn.timeoff.security.model.User;

public interface GroupAuthorityRepository extends CrudRepository<GroupAuthority, Long> {
    
    @Query(   "select ga from GroupAuthority ga join ga.group g, GroupMember gm join gm.user u "
            + "where gm.group = g and g.domain = u.domain and u = :user")
    public List<GroupAuthority> findByUser(@Param("user") User user);

    @Query(   "select ga.authority from GroupAuthority ga join ga.group g join g.domain d "
            + "where d.name=:doName and g.name=:groupname")
    public List<String> findAuthoritiesByDomainNameAndGroupname(@Param("doName") String doName,
                                                                @Param("groupname") String groupname);

    @Query(   "select ga from GroupAuthority ga join ga.group g join g.domain d "
            + "where d.name=:doName and g.name=:groupname and ga.authority=:authority")
    public List<GroupAuthority> findByDomainNameAndGroupnameAndAuthority(
                        @Param("doName") String doName, @Param("groupname") String groupname,
                        @Param("authority") String authority);
}
