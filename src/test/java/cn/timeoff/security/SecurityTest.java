package cn.timeoff.security;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
import cn.timeoff.security.service.CooperationUserDetailsManager;

@SpringApplicationConfiguration(classes = {cn.timeoff.Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles("dev")
public class  SecurityTest{

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

	@Autowired
	private UserDetailsService coUserDetailsManager;

	@Before
	public void setUp() throws Exception {
		groupAuthorityRepository.deleteAll();
		groupMemberRepository.deleteAll();
		groupRepository.deleteAll();
		authorityRepository.deleteAll();
		userRepository.deleteAll();
		cooperationRepository.deleteAll();

		Cooperation co = new Cooperation();
		co.setName("Timeoff");
		cooperationRepository.save(co);
		
        User jack = new User("Jack", "jack@24.hours", "");
        jack.setPassword("jack");
        jack.setCooperation(co);
        userRepository.save(jack);

        Group user_group = new Group();
        user_group.setCooperation(co);
        user_group.setGroupName("USER");
        groupRepository.save(user_group);
        
        Group admin_group = new Group();
        admin_group.setCooperation(co);
        admin_group.setGroupName("ADMIN");
        groupRepository.save(admin_group);
        
        GroupAuthority user_authority = new GroupAuthority();
        user_authority.setAuthority("USER");
        user_authority.setGroup(user_group);
        groupAuthorityRepository.save(user_authority);

        GroupAuthority admin_authority = new GroupAuthority();
        admin_authority.setAuthority("ADMIN");
        admin_authority.setGroup(admin_group);
        groupAuthorityRepository.save(admin_authority);

        GroupMember user_member = new GroupMember();
        user_member.setGroup(user_group);
        user_member.setUser(jack);
        groupMemberRepository.save(user_member);

        GroupMember admin_member = new GroupMember();
        admin_member.setGroup(admin_group);
        admin_member.setUser(jack);
        groupMemberRepository.save(admin_member);
        
	}

	@Test
	public void basicSecurity() {
		UserDetails jack_details = coUserDetailsManager.loadUserByUsername("Jack");
		@SuppressWarnings("unchecked")
		List<GrantedAuthority> auths = (List<GrantedAuthority>) jack_details.getAuthorities();
		org.junit.Assert.assertEquals(2, auths.size());
		org.junit.Assert.assertEquals("ROLE_ADMIN", auths.get(0).getAuthority());
		org.junit.Assert.assertEquals("ROLE_USER", auths.get(1).getAuthority());
	}

}
