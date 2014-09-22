package cn.timeoff.repository;

import org.springframework.data.repository.CrudRepository;

import cn.timeoff.model.TimeoffSetting;

public interface TimeoffSettingRespository extends
		CrudRepository<TimeoffSetting, Long> {

}
