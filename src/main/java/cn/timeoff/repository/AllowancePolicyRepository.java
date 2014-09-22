package cn.timeoff.repository;

import org.springframework.data.repository.CrudRepository;

import cn.timeoff.model.AllowancePolicy;

public interface AllowancePolicyRepository extends
		CrudRepository<AllowancePolicy, Long> {

}
