package cn.timeoff.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import cn.timeoff.model.Authority;
import cn.timeoff.model.User;
import cn.timeoff.repository.AuthorityRepository;
import cn.timeoff.repository.UserRepository;


@SpringApplicationConfiguration(classes = {cn.timeoff.Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles("dev")
public class AuthorityRespsitoryTest {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthorityRepository authorityRepository;
	
	@Before
	public void cleanDB() {
		userRepository.deleteAll();
		authorityRepository.deleteAll();
	}

	@Test
	public void basic() {
        // save a couple of customers
        User jack = new User("Jack", "jack@24.com", "");
        jack.setPassword("jack");
        userRepository.save(jack);
        
        Authority authority = new Authority("USER");
        authority.setUser(jack);
        authorityRepository.save(authority);
        
        assert jack == userRepository.findByUsername("Jack").get(0);
	}

}
