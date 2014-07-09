package cn.timeoff.security.repository;

import org.springframework.data.repository.CrudRepository;

import cn.timeoff.security.model.Group;

public interface GroupRepository extends CrudRepository<Group, Long> {

}
