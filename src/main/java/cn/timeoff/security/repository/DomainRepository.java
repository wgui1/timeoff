package cn.timeoff.security.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import cn.timeoff.security.model.Domain;

public interface DomainRepository extends CrudRepository<Domain, Long> {
	public List<Domain> findByName(String name);
}
