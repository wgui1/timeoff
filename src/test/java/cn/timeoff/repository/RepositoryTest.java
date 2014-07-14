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
import cn.timeoff.security.model.Employee;
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
import cn.timeoff.security.repository.EmployeeRepository;

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

	@Autowired
	private EmployeeRepository employeeRepository;

	@Before
	public void cleanDB() {
		groupAuthorityRepository.deleteAll();
		groupMemberRepository.deleteAll();
		groupRepository.deleteAll();
		authorityRepository.deleteAll();
		employeeRepository.deleteAll();
		userRepository.deleteAll();
		cooperationRepository.deleteAll();
	}

	@Test
	public void basicUser() {
        // save a couple of customers
        User user_jack = new User("Jack", "jack@24.com", "");
        user_jack.setPassword("jack");
        userRepository.save(user_jack);
        User user_amanda = new User("Amanda", "amanda@24.com", "");
        user_amanda.setPassword("amanda");
        user_amanda = userRepository.save(user_amanda);
	}

	@Test
	public void basicAuthority() {
        // save a couple of customers
        User user_jack = new User("Jack", "jack@24.com", "");
        user_jack.setPassword("jack");
        user_jack = userRepository.save(user_jack);
        
        Authority authority = new Authority(user_jack, "USER");
        authority.setUser(user_jack);
        authorityRepository.save(authority);
        
        assert user_jack == userRepository.findByUsername("Jack").get(0);
	}

	@Test
	public void basicGroupAuthority() {
		
		Cooperation co = new Cooperation();
		co.setName("Timeoff");
		cooperationRepository.save(co);
		
        User jack = new User("Jack", "jack@24.com", "");
        jack.setPassword("jack");
        jack = userRepository.save(jack);
        
        Employee employee_jack = new Employee(jack, co);
        employee_jack = employeeRepository.save(employee_jack);

        Group user_group = new Group();
        user_group.setCooperation(co);
        user_group.setGroupName("USER");
        user_group = groupRepository.save(user_group);
        
        GroupMember group_member = new GroupMember();
        group_member.setGroup(user_group);
        group_member.setEmployee(employee_jack);
        group_member = groupMemberRepository.save(group_member);
        
        GroupAuthority group_authority = new GroupAuthority();
        group_authority.setAuthority("USER");
        group_authority.setGroup(user_group);
        group_authority = groupAuthorityRepository.save(group_authority);
        
        List<GroupAuthority> authorities = groupAuthorityRepository.findByEmployee(employee_jack);
        org.junit.Assert.assertFalse(authorities.isEmpty());
        org.junit.Assert.assertEquals(authorities.get(0).getId(), group_authority.getId());
        org.junit.Assert.assertEquals(authorities.get(0).getAuthority(), "USER");

	}

}