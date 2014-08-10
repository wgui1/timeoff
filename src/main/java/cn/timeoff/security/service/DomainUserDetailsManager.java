package cn.timeoff.security.service;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;

public interface DomainUserDetailsManager extends UserDetailsManager{
    List<String> findAllGroups(String doName);

    List<String> findUsersInGroup(String doName, String groupName);

    void createDomain(String doName);

    void deleteDomain(String domainName);

    void createGroup(String doName, String groupName, List<GrantedAuthority> authorities);

    void deleteGroup(String doName, String groupName);

    void renameGroup(String doName, String oldName, String newName);

    void addUserToGroup(String doName, String username, String group);

    void removeUserFromGroup(String doName, String username, String groupName);

    List<GrantedAuthority> findGroupAuthorities(String doName, String groupName);

    void addGroupAuthority(String doName, String groupName, GrantedAuthority authority);

    void removeGroupAuthority(String doName, String groupName, GrantedAuthority authority);

    void deleteUser(String domainName, String username);

    boolean userExists(String domainName, String username);

    UserDetails loadUserByDomainNameAndUsername(String domainName, String username);
}
