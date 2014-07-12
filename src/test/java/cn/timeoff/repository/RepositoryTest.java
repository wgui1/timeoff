package cn.timeoff.repository;

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
import cn.timeoff.security.model.Cooperation;
import cn.timeoff.security.model.Group;
import cn.timeoff.security.model.GroupAuthority;
import cn.timeoff.security.model.GroupMember;
import cn.timeoff.security.model.User;
import cn.timeoff.security.repository.AuthorityRepository;
import cn.timeoff.security.repository.CooperationRepository;
import cn.timeoff.security.repository.GroupAuthorityRepository;
import cn.timeoff.security.repository.GroupMemberRepository;
import cn.timeoff.security.repository.GroupRepository;
import cn.timeoff.security.repository.UserRepository;

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
	private CooperationRepository cooperationRepository;

	@Before
	public void cleanDB() {
		groupAuthorityRepository.deleteAll();
		groupMemberRepository.deleteAll();
		groupRepository.deleteAll();
		authorityRepository.deleteAll();
		userRepository.deleteAll();
	}

	@Test
	public void basicUser() {
        // save a couple of customers
        User jack = new User("Jack", "jack@24.com", "");
        jack.setPassword("jack");
        userRepository.save(jack);
        User amanda = new User("Amanda", "amanda@24.com", "");
        amanda.setPassword("amanda");
        userRepository.save(amanda);
	}

	@Test
	public void basicAuthority() {
        // save a couple of customers
        User jack = new User("Jack", "jack@24.com", "");
        jack.setPassword("jack");
        userRepository.save(jack);
        
        Authority authority = new Authority("USER");
        authority.setUser(jack);
        authorityRepository.save(authority);
        
        assert jack == userRepository.findByUsername("Jack").get(0);
	}

	@Test
	public void basicGroupAuthority() {
		
		Cooperation co = new Cooperation();
		co.setName("Timeoff");
		cooperationRepository.save(co);
		
        User jack = new User("Jack", "jack@24.com", "");
        jack.setPassword("jack");
        jack.setCooperation(co);
        userRepository.save(jack);

        Group user_group = new Group();
        user_group.setCooperation(co);
        user_group.setGroupName("USER");
        groupRepository.save(user_group);
        
        GroupMember group_member = new GroupMember();
        group_member.setGroup(user_group);
        group_member.setUser(jack);
        groupMemberRepository.save(group_member);
        
        GroupAuthority group_authority = new GroupAuthority();
        group_authority.setAuthority("USER");
        group_authority.setGroup(user_group);
        groupAuthorityRepository.save(group_authority);
        
        List<GroupAuthority> authorities = groupAuthorityRepository.findByUser(jack);
        org.junit.Assert.assertFalse(authorities.isEmpty());
        org.junit.Assert.assertEquals(authorities.get(0).getId(), group_authority.getId());

	}

}