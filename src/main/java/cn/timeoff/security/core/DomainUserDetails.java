package cn.timeoff.security.core;

import org.springframework.security.core.userdetails.UserDetails;

public interface DomainUserDetails extends UserDetails {
	
	public String getEmail();

	public String getDomainName();

	public void setDomainName(String domainName);
	
}
