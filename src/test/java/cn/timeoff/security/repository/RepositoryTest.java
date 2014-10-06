package cn.timeoff.security.repository;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import cn.timeoff.security.model.Authority;
import cn.timeoff.security.model.Domain;
import cn.timeoff.security.model.Group;
import cn.timeoff.security.model.GroupAuthority;
import cn.timeoff.security.model.GroupMember;
import cn.timeoff.security.model.User;

@SpringApplicationConfiguration(classes = {cn.timeoff.Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles("dev")
public class RepositoryTest {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupAuthorityRepository groupAuthorityRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;
    
    @Autowired
    private GroupRepository groupRepository;
    
    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private DomainRepository cooperationRepository;

    @Before
    public void cleanDB() {
        groupAuthorityRepository.deleteAll();
        groupMemberRepository.deleteAll();
        groupRepository.deleteAll();
        authorityRepository.deleteAll();
        userRepository.deleteAll();
        cooperationRepository.deleteAll();
    }

    @Test
    public void basicUser() {
        Domain domain = new Domain();
        domain.setName("Timeoff");
        domain = cooperationRepository.save(domain);

        // save a couple of customers
        User user_jack = new User(domain, "Jack", "jack@24.com", "");
        user_jack.setPassword("jack");
        userRepository.save(user_jack);
        User user_amanda = new User(domain, "Amanda", "amanda@24.com", "");
        user_amanda.setPassword("amanda");
        user_amanda = userRepository.save(user_amanda);
    }

    @Test
    public void basicAuthority() {
        Domain domain = new Domain();
        domain.setName("Timeoff");
        domain = cooperationRepository.save(domain);

        // save a couple of customers
        User user_jack = new User(domain, "Jack", "jack@24.com", "");
        user_jack.setPassword("jack");
        user_jack = userRepository.save(user_jack);
        
        Authority authority = new Authority(user_jack, "USER");
        authority.setUser(user_jack);
        authorityRepository.save(authority);
        
        org.junit.Assert.assertEquals(user_jack.getUsername(),
                    userRepository.findByDomainNameAndUsername(
                        "Timeoff", "Jack").get(0).getUsername());
    }

    @Test
    public void basicGroup() {
        
        Domain domain = new Domain();
        domain.setName("Timeoff");
        cooperationRepository.save(domain);
        
        Group user_group = new Group();
        user_group.setDomain(domain);
        user_group.setName("USER");
        user_group = groupRepository.save(user_group);
        
        Group admin_group = new Group();
        admin_group.setDomain(domain);
        admin_group.setName("ADMIN");
        admin_group = groupRepository.save(admin_group);
        
        List<String> groups = groupRepository.findNamesByDomainName("Timeoff");
        org.junit.Assert.assertEquals(2, groups.size());
        org.junit.Assert.assertEquals("ADMIN", groups.get(0));
        org.junit.Assert.assertEquals("USER", groups.get(1));

        List<Group> groupss = groupRepository.findByDomainNameAndGroupName("Timeoff", "USER");
        org.junit.Assert.assertEquals(1, groupss.size());
        org.junit.Assert.assertEquals("USER", groupss.get(0).getName());
    }

    @Test
    public void basicGroupAuthority() {
        
        Domain domain = new Domain();
        domain.setName("Timeoff");
        cooperationRepository.save(domain);
        
        User jack = new User(domain, "Jack", "jack@24.com", "");
        jack.setPassword("jack");
        jack = userRepository.save(jack);
        
        Group user_group = new Group();
        user_group.setDomain(domain);
        user_group.setName("USER");
        user_group = groupRepository.save(user_group);
        
        GroupMember group_member = new GroupMember();
        group_member.setGroup(user_group);
        group_member.setUser(jack);
        group_member = groupMemberRepository.save(group_member);
        
        GroupAuthority group_authority = new GroupAuthority();
        group_authority.setAuthority("USER");
        group_authority.setGroup(user_group);
        group_authority = groupAuthorityRepository.save(group_authority);
        
        List<GroupAuthority> authorities = groupAuthorityRepository.findByUser(jack);
        org.junit.Assert.assertFalse(authorities.isEmpty());
        org.junit.Assert.assertEquals(authorities.get(0).getId(), group_authority.getId());
        org.junit.Assert.assertEquals(authorities.get(0).getAuthority(), "USER");

    }

    @Test
    public void groupMember() {
        
        Domain domain = new Domain();
        domain.setName("Timeoff");
        cooperationRepository.save(domain);
        
        User jack = new User(domain, "Jack", "jack@24.com", "");
        jack.setPassword("jack");
        jack = userRepository.save(jack);

        User nina = new User(domain, "Nina", "nina@24.com", "");
        nina.setPassword("nina");
        nina = userRepository.save(nina);
        
        Group user_group = new Group();
        user_group.setDomain(domain);
        user_group.setName("USER");
        user_group = groupRepository.save(user_group);
        
        GroupMember group_member = new GroupMember();
        group_member.setGroup(user_group);
        group_member.setUser(jack);
        group_member = groupMemberRepository.save(group_member);

        GroupMember group_member_1 = new GroupMember();
        group_member_1.setGroup(user_group);
        group_member_1.setUser(nina);
        group_member_1 = groupMemberRepository.save(group_member_1);
        
        List<String> members = groupMemberRepository.
                                findMembersByDomainNameAndGroupname("Timeoff", "USER");
        org.junit.Assert.assertEquals(2, members.size());
        org.junit.Assert.assertEquals("Jack", members.get(0));
        org.junit.Assert.assertEquals("Nina", members.get(1));
        
        List<GroupMember> gms = groupMemberRepository.
                findByDomainNameAndGroupnameAndUsername("Timeoff", "Jack", "USER");
        org.junit.Assert.assertEquals(1, gms.size());
        org.junit.Assert.assertEquals("Jack", gms.get(0).getUser().getUsername());
    }
}