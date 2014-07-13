package cn.timeoff.security.core;

import org.springframework.security.core.userdetails.UserDetails;

import cn.timeoff.security.model.Employee;

public interface CooperationUserDetails extends UserDetails {
	
	public String getEmail();

	public Employee getEmployee();
	
	public void setEmployee(Employee employee);
	
	public boolean isEmployee();
	
	public String getCooperationName();

	public void setCooperationName(String cooperationName);
	
	public boolean hasCooperationName();

}
