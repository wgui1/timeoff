package cn.timeoff.security.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.util.Assert;

import cn.timeoff.security.core.DomainAuthentication;
import cn.timeoff.security.core.DomainNotFoundException;
import cn.timeoff.security.core.DomainSecurityAuthenticationToken;
import cn.timeoff.security.core.DomainUserDetails;
import cn.timeoff.security.core.UserNotFoundException;
import cn.timeoff.security.core.GroupAuthorityNotFoundException;
import cn.timeoff.security.core.GroupMemberNotFoundException;
import cn.timeoff.security.core.GroupNotFoundException;
import cn.timeoff.security.model.Authority;
import cn.timeoff.security.model.Domain;
import cn.timeoff.security.model.Group;
import cn.timeoff.security.model.GroupAuthority;
import cn.timeoff.security.model.GroupMember;
import cn.timeoff.security.model.User;
import cn.timeoff.security.repository.AuthorityRepository;
import cn.timeoff.security.repository.DomainRepository;
import cn.timeoff.security.repository.GroupAuthorityRepository;
import cn.timeoff.security.repository.GroupMemberRepository;
import cn.timeoff.security.repository.GroupRepository;
import cn.timeoff.security.repository.UserRepository;

@Transactional
public class DomainUserDetailsManagerImpl extends DomainUserDetailsServiceImpl
                                          implements DomainUserDetailsManager {
    
    @Autowired
    private DomainRepository domainRepository; 

    @Autowired
    private UserRepository userRepository; 

    @Autowired
    private AuthorityRepository authorityRepository; 

    @Autowired
    private GroupRepository groupRepository; 
    
    @Autowired
    private GroupMemberRepository groupMemberRepository; 

    @Autowired
    private GroupAuthorityRepository groupAuthorityRepository; 

    @Autowired
    private AuthenticationManager authenticationManager;

    private UserCache userCache = new NullUserCache();

    @Override
    public void createUser(UserDetails userDetails){
        DomainUserDetails doUserDetails = (DomainUserDetails) userDetails;
        String domainName = doUserDetails.getDomainName();
        Domain domain = findDomain(domainName);
        User dbUser = new User(domain, doUserDetails.getUsername(),
                               doUserDetails.getEmail(),
                               doUserDetails.getPassword());
        dbUser.setEnabled(doUserDetails.isEnabled());
        dbUser = userRepository.save(dbUser);

        if (getEnableAuthorities()) {
            insertUserAuthorities(dbUser, userDetails);
        }
    }

    @Override
    public void updateUser(UserDetails userDetails) {
        DomainUserDetails doUserDetails = (DomainUserDetails) userDetails;
        User dbUser = findUser(doUserDetails.getDomainName(),
                               doUserDetails.getUsername());
        dbUser.setEnabled(doUserDetails.isEnabled());
        dbUser.setEmail(doUserDetails.getEmail());

        dbUser = userRepository.save(dbUser);

        if (getEnableAuthorities()) {
            authorityRepository.deleteByUser(dbUser);
            insertUserAuthorities(dbUser, userDetails);
        }
    }

    @Override
    public void deleteUser(String domainName, String username) {
        User user = findUser(domainName, username);
        userRepository.delete(user);
    }

    @Override
    public void deleteUser(String username) {
        deleteUser(defaultDomainName, username);
    }
    
    private void insertUserAuthorities(User user, UserDetails userDetails) {
        for (GrantedAuthority auth : userDetails.getAuthorities()) {
            authorityRepository.save(new Authority(user, auth.getAuthority()));
        }
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null) {
            throw new AccessDeniedException(
                    messages.getMessage("DomainUserDetailsService.AccessDenied",
                      "Can't change password as no DomainAuthentication object found in context "
                    + "for current user."));
        }
        if (currentUser instanceof DomainAuthentication) {
             throw new AccessDeniedException(
                    messages.getMessage("DomainUserDetailsService.AccessDenied",
                      "Can't change password as no DomainAuthentication object found in context "
                    + "for current user."));           
        }
        DomainAuthentication domainUser = (DomainAuthentication) currentUser;

        String username = domainUser.getName();
        String domainName = domainUser.getDomain();
        User user = findUser(domainName, username);

        // If an authentication manager has been set, re-authenticate the user with the supplied password.
        if (authenticationManager != null) {
            logger.debug("Reauthenticating user '"+ username + "' for password change request.");

            authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(username, oldPassword));
        } else {
            logger.debug("No authentication manager set. Password won't be re-checked.");
        }

        logger.debug("Changing password for user '"+ username + "'");

        user.setPassword(newPassword);
        userRepository.save(user);

        SecurityContextHolder.getContext().setAuthentication(
                                    createNewAuthentication(domainUser, newPassword));

        userCache.removeUserFromCache(username);

    }

    protected Authentication createNewAuthentication(DomainAuthentication currentAuth,
                                                     String newPassword) {
        UserDetails user = loadUserByDomainNameAndUsername(currentAuth.getDomain(),
                                                           currentAuth.getName());

        DomainSecurityAuthenticationToken newAuthentication =
                new DomainSecurityAuthenticationToken(user, null, user.getAuthorities(),
                                                      currentAuth.getDomain());
        newAuthentication.setDetails(currentAuth.getDetails());
        return newAuthentication;
    }

    @Override
    public boolean userExists(String domainName, String username) {
        List<User> users = userRepository.findByDomainNameAndUsername(domainName, username);
        return !users.isEmpty();
    }

    @Override
    public boolean userExists(String username) {
        return userExists(defaultDomainName, username);
    }

    /**
     * Optionally sets the UserCache if one is in use in the application.
     * This allows the user to be removed from the cache after updates have taken place to avoid stale data.
     *
     * @param userCache the cache used by the AuthenticationManager.
     */
    public void setUserCache(UserCache userCache) {
        Assert.notNull(userCache, "userCache cannot be null");
        this.userCache = userCache;
    }

    @Override
    public void createDomain(String doName) {
        domainRepository.save(new Domain(doName));
    }

    @Override
    public void deleteDomain(String doName) {
        Domain co = findDomain(doName);
        domainRepository.delete(co);
    }

    @Override
    public List<String> findAllGroups(String doName) {
        return groupRepository.findNamesByDomainName(doName);
    }

    @Override
    public List<String> findUsersInGroup(String doName, String groupname) {
        return groupMemberRepository.
                findMembersByDomainNameAndGroupname(doName, groupname);
    }

    @Override
    public void createGroup(String doName, String groupname,
                            List<GrantedAuthority> authorities) {
        Domain domain = findDomain(doName);
        Group group = new Group(domain, groupname);
        final Group group1 = groupRepository.save(group);
        authorities.stream().map(p -> groupAuthorityRepository.save(
                        new GroupAuthority(group1, p.getAuthority())));
    }

    @Override
    public void deleteGroup(String doName, String groupname) {
        Group group = findGroup(doName, groupname);
        groupRepository.delete(group);
    }

    @Override
    public void renameGroup(String doName, String oldName, String newName) {
        Group group = findGroup(doName, oldName);
        group.setName(newName);
        groupRepository.save(group);
    }

    @Override
    public void addUserToGroup(String doName, String groupname, String username) {
        Group group = findGroup(doName, groupname);
        User user = findUser(doName, username);
        GroupMember gm = new GroupMember(group, user);
        groupMemberRepository.save(gm);
    }

    @Override
    public void removeUserFromGroup(String doName, String groupname,
                                        String username) {
        GroupMember gm = findGroupMember(doName, groupname, username);
        groupMemberRepository.delete(gm);
    }

    @Override
    public List<GrantedAuthority> findGroupAuthorities(String doName,
                                                       String groupname) {
        return groupAuthorityRepository.findAuthoritiesByDomainNameAndGroupname(
                doName, groupname).stream()
                                  .map(p -> new SimpleGrantedAuthority(p))
                                  .collect(Collectors.toList());
    }

    @Override
    public void addGroupAuthority(String doName, String groupname,
                                  GrantedAuthority authority) {
        Group group = findGroup(doName, groupname);
        GroupAuthority ga = new GroupAuthority(group, authority.getAuthority());
        groupAuthorityRepository.save(ga);
    }

    @Override
    public void removeGroupAuthority(String doName, String groupname,
                                     GrantedAuthority authority) {
        GroupAuthority ga = findGroupAuthority(doName, groupname, authority.getAuthority());
        groupAuthorityRepository.delete(ga);
    }

    protected Domain findDomain(String domainName)
            throws DomainNotFoundException {
        List<Domain> domains = domainRepository.findByName(domainName);
        if (domains.isEmpty()) {
            logger.debug("No resuts found for domain '" + domainName + "'");
            throw new DomainNotFoundException(
                messages.getMessage("DomainUserDetailsService.DomainNotFound",
                                new Object[]{domainName}, "Domain {0} not found"));
            
        }
        return domains.get(0);
    }

    protected Group findGroup(String doName, String groupname)
            throws GroupNotFoundException {
        List<Group> groups = groupRepository.
                findByDomainNameAndGroupName(doName, groupname);
        if (groups.isEmpty()) {
            logger.debug(  "No resuts found for domain '" + doName + "'"
                         + " and group '" + groupname + "'");
            throw new GroupNotFoundException(
                messages.getMessage("DomainUserDetailsService.GroupNotFound",
                                new Object[]{groupname, doName}, "Group {0} under Domain {1} not found"));
            
        }
        return groups.get(0);
    }

    protected User findUser(String doName, String username)
            throws UserNotFoundException {
        List<User> users = userRepository.
                findByDomainNameAndUsername(doName, username);
        if (users.isEmpty()) {
            logger.debug(  "No resuts found for domain '" + doName + "'"
                         + " and user '" + username + "'");
            throw new UserNotFoundException(
                messages.getMessage("DomainUserDetailsService.EmployeeNotFound",
                                new Object[]{username, doName},
                                "User {0} under Domain {1} not found"));
            
        }
        return users.get(0);
    }

    protected GroupAuthority findGroupAuthority(String doName, String groupname, String authority)
            throws GroupAuthorityNotFoundException {
        List<GroupAuthority> groupAuthorities = groupAuthorityRepository.
                findByDomainNameAndGroupnameAndAuthority(doName, groupname, authority);
        if (groupAuthorities.isEmpty()) {
            logger.debug(  "No resuts found for domain '" + doName + "'"
                         + " and group '" + groupname + "'"
                         + " and authority '" + authority + "'");
            throw new GroupAuthorityNotFoundException(
                messages.getMessage("DomainUserDetailsService.GroupAuthorityNotFound",
                                new Object[]{authority, groupname, doName},
                                "Authority {0} under Group {1} Domain {2} not found"));
            
        }
        GroupAuthority ga = groupAuthorities.get(0);
        return ga;
    }


    protected GroupMember findGroupMember(String doName, String groupname, String username)
            throws GroupMemberNotFoundException {
        List<GroupMember> groupMembers = groupMemberRepository.
                findByDomainNameAndGroupnameAndUsername(doName, groupname, username);
        if (groupMembers.isEmpty()) {
            logger.debug(  "No resuts found for domain '" + doName + "'"
                         + " and group '" + groupname + "'"
                         + " and user '" + username + "'");
            throw new GroupMemberNotFoundException(
                messages.getMessage("DomainUserDetailsService.GroupMemberNotFound",
                        new Object[]{username, groupname, doName},
                        "Employee {0} under Group {1} Domain {2} not found"));
            
        }
        GroupMember groupMember = groupMembers.get(0);
        return groupMember;
    }
}
