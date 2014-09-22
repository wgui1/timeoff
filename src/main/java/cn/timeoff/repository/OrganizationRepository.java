package cn.timeoff.repository;

import org.springframework.data.repository.CrudRepository;

import cn.timeoff.model.Organization;

public interface OrganizationRepository extends
		CrudRepository<Organization, Long> {

}
