package cn.timeoff.repository;

import org.springframework.data.repository.CrudRepository;

import cn.timeoff.model.PartialYearRate;

public interface PartialYearRateRepository extends
		CrudRepository<PartialYearRate, Long> {

}
