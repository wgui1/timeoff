package cn.timeoff.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import cn.timeoff.security.core.DomainUserDetails;
import cn.timeoff.security.core.DomainUserDetailsImpl;
import cn.timeoff.security.core.UserNotFoundException;
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
import cn.timeoff.security.service.DomainUserDetailsManager;

@SpringApplicationConfiguration(classes = {cn.timeoff.Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles("dev")
@Transactional
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
	private DomainRepository domainRepository;

	@Autowired
	private DomainUserDetailsManager userDetailsManager;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void basicSecurity() {
		Domain domain = new Domain();
		domain.setName("Timeoff");
		domain = domainRepository.save(domain);
		
        User jack = new User(domain, "Jack", "jack@24.hours", "");
        jack.setPassword("jack");
        jack = userRepository.save(jack);

        Group user_group = new Group();
        user_group.setDomain(domain);
        user_group.setName("USER");
        user_group = groupRepository.save(user_group);
        
        Group admin_group = new Group();
        admin_group.setDomain(domain);
        admin_group.setName("ADMIN");
        admin_group = groupRepository.save(admin_group);
        
        GroupAuthority user_authority = new GroupAuthority();
        user_authority.setAuthority("USER");
        user_authority.setGroup(user_group);
        user_authority = groupAuthorityRepository.save(user_authority);

        GroupAuthority admin_authority = new GroupAuthority();
        admin_authority.setAuthority("ADMIN");
        admin_authority.setGroup(admin_group);
        admin_authority = groupAuthorityRepository.save(admin_authority);

        GroupMember user_member = new GroupMember();
        user_member.setGroup(user_group);
        user_member.setUser(jack);
        user_member = groupMemberRepository.save(user_member);

        GroupMember admin_member = new GroupMember();
        admin_member.setGroup(admin_group);
        admin_member.setUser(jack);
        admin_member = groupMemberRepository.save(admin_member);
        
		UserDetails jack_details = userDetailsManager.loadUserByDomainNameAndUsername("Timeoff", "Jack");
		@SuppressWarnings("unchecked")
		List<GrantedAuthority> auths = (List<GrantedAuthority>) jack_details.getAuthorities();
		org.junit.Assert.assertEquals(2, auths.size());
		org.junit.Assert.assertEquals("ROLE_ADMIN", auths.get(0).getAuthority());
		org.junit.Assert.assertEquals("ROLE_USER", auths.get(1).getAuthority());
	}

	@Test
	public void createUser() {
		Domain domain = new Domain();
		domain.setName("Timeoff");
		domain = domainRepository.save(domain);

		DomainUserDetails user = new DomainUserDetailsImpl("Timeoff",
				"Jack", "1234", "jack@24.hours", true, true, true, true,
				Arrays.asList( new SimpleGrantedAuthority("USER"),
							   new SimpleGrantedAuthority("ADMIN") ));
		userDetailsManager.createUser(user);
		UserDetails jack_details = userDetailsManager.loadUserByDomainNameAndUsername("Timeoff", "Jack");
		org.junit.Assert.assertEquals(2, jack_details.getAuthorities().size());
		List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>(jack_details.getAuthorities());
		org.junit.Assert.assertEquals("ROLE_ADMIN", auths.get(0).getAuthority());
		org.junit.Assert.assertEquals("ROLE_USER", auths.get(1).getAuthority());
	}
		
	@Test(expected=UserNotFoundException.class)
	public void deleteUser() {
		Domain domain = new Domain();
		domain.setName("Timeoff");
		domain = domainRepository.save(domain);

		DomainUserDetails user = new DomainUserDetailsImpl("Timeoff",
				"Jack", "1234", "jack@24.hours", true, true, true, true,
				Arrays.asList( new SimpleGrantedAuthority("USER") ));
		userDetailsManager.createUser(user);
		userDetailsManager.deleteUser("Timeoff", "Jack");
		userDetailsManager.loadUserByDomainNameAndUsername("Timeoff", "Jack");
	}
		
	@Test
	public void updateUser() {
		Domain domain = new Domain();
		domain.setName("Timeoff");
		domain = domainRepository.save(domain);

		DomainUserDetails user = new DomainUserDetailsImpl("Timeoff",
				"Jack", "1234", "jack@24.hours", true, true, true, true,
				Arrays.asList( new SimpleGrantedAuthority("USER") ));
		userDetailsManager.createUser(user);
		
		DomainUserDetails user1 = new DomainUserDetailsImpl("Timeoff",
				"Jack", "1234", "jack@24.com", true, true, true, true,
				Arrays.asList( new SimpleGrantedAuthority("ADMIN") ));
		userDetailsManager.updateUser(user1);

		UserDetails jack_details = userDetailsManager.loadUserByDomainNameAndUsername("Timeoff", "Jack");
		org.junit.Assert.assertEquals("ROLE_ADMIN", jack_details.getAuthorities().
											   iterator().next().getAuthority());
		org.junit.Assert.assertEquals("Jack", jack_details.getUsername());
		org.junit.Assert.assertEquals("jack@24.com", ((DomainUserDetails) jack_details).getEmail());
	}

	@Test
	public void userExists() {
		Domain domain = new Domain();
		domain.setName("Timeoff");
		domain = domainRepository.save(domain);
		DomainUserDetails user = new DomainUserDetailsImpl("Timeoff",
				"Jack", "1234", "jack@24.hours", true, true, true, true,
				Arrays.asList( new SimpleGrantedAuthority("USER") ));
		userDetailsManager.createUser(user);
		
		org.junit.Assert.assertTrue(userDetailsManager.userExists("Timeoff", "Jack"));
		
	}
	
	@Test
	public void basicGroups() {
		Domain domain = new Domain();
		domain.setName("Timeoff");
		domain = domainRepository.save(domain);
        userDetailsManager.createDomain("Timeoff");
		userDetailsManager.createGroup("Timeoff", "EMPLOYEE",
								   Arrays.asList( new SimpleGrantedAuthority("EMPLOYEE")));
		List<String> groups = userDetailsManager.findAllGroups("Timeoff");
		org.junit.Assert.assertEquals(1, groups.size());
		org.junit.Assert.assertEquals("EMPLOYEE", groups.iterator().next());

	}
		
}
