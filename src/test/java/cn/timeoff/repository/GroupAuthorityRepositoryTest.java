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
public class GroupAuthorityRepositoryTest {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private GroupAuthorityRepository groupAuthorityRepository;

	@Autowired
	private GroupMemberRepository groupMemberRepository;
	
	@Autowired
	private GroupRepository groupRepository;
	
	@Autowired
	private CooperationRepository cooperationRepository;

	@Before
	public void cleanDB() {
		userRepository.deleteAll();
		groupAuthorityRepository.deleteAll();
		groupMemberRepository.deleteAll();
		groupRepository.deleteAll();
	}

	@Test
	public void basic() {
		
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
        
        List<GroupAuthority> authorities = groupAuthorityRepository.findByUser(jack);
        org.junit.Assert.assertFalse(authorities.isEmpty());
        org.junit.Assert.assertEquals(authorities.get(0), group_authority);

	}

	public void basic2() {
		
		Cooperation co = new Cooperation();
		co.setName("Timeoff");
		cooperationRepository.save(co);
		
        User jack = new User("Jack", "jack@24.com", "");
        jack.setPassword("jack");
        jack.setCooperation(co);
        userRepository.save(jack);

        User amanda = new User("Amanda", "amanda@24.com", "");
        amanda.setPassword("amanda");
        amanda.setCooperation(co);
        userRepository.save(amanda);

        Group user_group = new Group();
        user_group.setCooperation(co);
        user_group.setGroupName("USER");
        groupRepository.save(user_group);

        Group admin_group = new Group();
        admin_group.setCooperation(co);
        admin_group.setGroupName("ADMIN");
        groupRepository.save(admin_group);
        

	}
}