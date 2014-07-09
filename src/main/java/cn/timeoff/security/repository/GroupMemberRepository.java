package cn.timeoff.security.repository;

import org.springframework.data.repository.CrudRepository;

import cn.timeoff.security.model.GroupMember;

public interface GroupMemberRepository extends CrudRepository<GroupMember, Long>{

}
