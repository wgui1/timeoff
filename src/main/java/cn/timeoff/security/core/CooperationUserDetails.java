package cn.timeoff.security.core;

import org.springframework.security.core.userdetails.UserDetails;

import cn.timeoff.security.model.Employee;

public interface CooperationUserDetails extends UserDetails {
	
	public String getEmail();

	public Employee getEmployee();
	
	public void setEmployee(Employee employee);
	
	boolean isEmployee();

}
