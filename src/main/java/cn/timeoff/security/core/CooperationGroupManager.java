package cn.timeoff.security.core;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

public interface CooperationGroupManager {
    List<String> findAllGroups(String coName);

    List<String> findEmployeesInGroup(String coName, String groupName);

    void createGroup(String coName, String groupName, List<GrantedAuthority> authorities);

    void deleteGroup(String coName, String groupName);

    void renameGroup(String coName, String oldName, String newName);

    void addEmployeeToGroup(String coName, String username, String group);

    void removeEmployeeFromGroup(String coName, String username, String groupName);

    List<GrantedAuthority> findGroupAuthorities(String coName, String groupName);

    void addGroupAuthority(String coName, String groupName, GrantedAuthority authority);

    void removeGroupAuthority(String coName, String groupName, GrantedAuthority authority);
}
