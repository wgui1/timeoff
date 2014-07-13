package cn.timeoff.security.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import cn.timeoff.security.model.Cooperation;

public interface CooperationRepository extends CrudRepository<Cooperation, Long> {
	public List<Cooperation> findByName(String name);
}
