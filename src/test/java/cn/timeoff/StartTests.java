package cn.timeoff;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import cn.timeoff.model.User;
import cn.timeoff.repository.UserRepository;

/**
 * <p> Integration test using the jdbc profile. 
 * @see AbstractClinicServiceTests AbstractClinicServiceTests for more details. </p>
 *
 * @author Thomas Risberg
 * @author Michael Isvy
 */
@SpringApplicationConfiguration(classes = {cn.timeoff.Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles("dev")
public class StartTests extends AbstractJUnit4SpringContextTests{
	@Test
	public void basic() {
        UserRepository repository = applicationContext.getBean(UserRepository.class);

        // save a couple of customers
        User jack = new User("Jack", "jack@24.com", "");
        jack.setPassword("jack");
        repository.save(jack);
        User amanda = new User("Amanda", "amanda@24.com", "");
        amanda.setPassword("amanda");
        repository.save(amanda);
	}


}