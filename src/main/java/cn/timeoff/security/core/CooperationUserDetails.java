package cn.timeoff.security.core;

import org.springframework.security.core.userdetails.UserDetails;

public interface CooperationUserDetails extends UserDetails {
	
	public String getCooperation();

}
