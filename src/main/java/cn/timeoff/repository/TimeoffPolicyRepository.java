package cn.timeoff.repository;

import org.springframework.data.repository.CrudRepository;

import cn.timeoff.model.TimeoffPolicy;

public interface TimeoffPolicyRepository extends
		CrudRepository<TimeoffPolicy, Long> {

}
