package cn.timeoff.repository;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import cn.timeoff.core.NoValueSetError;
import cn.timeoff.model.Cooperation;
import cn.timeoff.model.Employee;
import cn.timeoff.model.Organization;
import cn.timeoff.model.PartialMonthRate;
import cn.timeoff.model.PartialYearRate;
import cn.timeoff.model.TimeoffSetting;
import cn.timeoff.model.AllowancePolicy;
import cn.timeoff.model.TimeoffPolicy;
import cn.timeoff.security.model.Domain;
import cn.timeoff.security.model.User;
import cn.timeoff.security.repository.DomainRepository;
import cn.timeoff.security.repository.UserRepository;

@SpringApplicationConfiguration(classes = {cn.timeoff.Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles("dev")
@Transactional
public class RepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private CooperationRepository cooperationRepository;

    @Autowired
    private AllowancePolicyRepository allowancePolicyRepository;

    @Autowired
    private PartialYearRateRepository partialYearRateRepository;

    @Autowired
    private TimeoffPolicyRepository timeoffPolicyRepository;

    @Autowired
    private TimeoffSettingRespository timeoffSettingRepository;

    @PersistenceContext
    private EntityManager entityManager;
 
    Domain domain;
    Cooperation cooperation;
    Organization organizationTop;

    @Before
    public void setup() {
        domain = new Domain();
        domain.setName("Timeoff");
        domain = domainRepository.save(domain);
        
        cooperation = new Cooperation();
        cooperation.setDomain(domain);
        cooperation = cooperationRepository.save(cooperation);
        
        organizationTop = new Organization(cooperation, "cooperation");
        organizationTop.setTopLevel(true);
        organizationTop = organizationRepository.save(organizationTop);
    }

    @Test
    public void organization() {
        Organization organizationIt = new Organization(cooperation, "IT");
        organizationIt.setUpperLevel(organizationTop);
        organizationIt = organizationRepository.save(organizationIt);

        Organization organizationHr = new Organization(cooperation, "HR");
        organizationHr.setUpperLevel(organizationTop);
        organizationHr = organizationRepository.save(organizationHr);

        Organization organizationUnix = new Organization(cooperation, "UNIX");
        organizationUnix.setUpperLevel(organizationIt);
        organizationUnix = organizationRepository.save(organizationUnix);
        
        boolean hasIt = false;
        boolean hasHr = false;
        Iterable<Organization> organizations = organizationRepository.findAll();
        for(Organization o: organizations) {
            entityManager.refresh(o);
        	if (o.getName() == "cooperation") {
                org.junit.Assert.assertTrue(o.isTopLevel());
                org.junit.Assert.assertEquals(o.getSubOrganizations().size(), 2);
                
        	}
        	if(o.getName() == "IT") {
        		hasIt = true;
                org.junit.Assert.assertEquals(o.getSubOrganizations().size(), 1);
                org.junit.Assert.assertEquals(o.getUpperLevel().getName(), "cooperation");
        	}
        	if(o.getName() == "UNIX") {
                org.junit.Assert.assertEquals(o.getUpperLevel().getName(), "IT");
        	}
        	if(o.getName() == "HR") {
        		hasHr = true;
                org.junit.Assert.assertEquals(o.getUpperLevel().getName(), "cooperation");
        	}
        }
        org.junit.Assert.assertTrue(hasIt);
        org.junit.Assert.assertTrue(hasHr);
    }

    @Test
    public void basicEmployee() {
        // save two employees
        User user_jack = new User(domain, "Jack", "jack@24.com", "");
        user_jack.setPassword("jack");
        user_jack = userRepository.save(user_jack);
        
        Employee employee_jack = new Employee(organizationTop, user_jack);
        employee_jack = employeeRepository.save(employee_jack);

        Iterable<Employee> employees = employeeRepository.findAll();
        Employee em_jack = employees.iterator().next();
        org.junit.Assert.assertEquals(em_jack.getUser().getUsername(), "Jack");
    }
    
    @Test
    public void partialYearRateTest() {
        TimeoffSetting timeoffSetting = new TimeoffSetting(organizationTop);
        timeoffSetting = timeoffSettingRepository.save(timeoffSetting);

        PartialYearRate partialYearRate = new PartialYearRate(timeoffSetting, true);
        partialYearRate = partialYearRateRepository.save(partialYearRate);
        
        Iterable<PartialYearRate> partialYearRates = partialYearRateRepository.findAll();
        partialYearRate = partialYearRates.iterator().next();
        org.junit.Assert.assertEquals(partialYearRate.getPropotional(), true);
        org.junit.Assert.assertEquals(partialYearRate.getPartialMonthRates().size(), 12);
        org.junit.Assert.assertEquals(partialYearRate.getPartialMonthRates().get(0).getMonth(), 1);
        org.junit.Assert.assertEquals(partialYearRate.getPartialMonthRates().get(11).getMonth(), 12);
        org.junit.Assert.assertEquals(partialYearRate.getPartialMonthRates().get(11).getRate(), 0.92, 0.01);
        
        ArrayList<PartialMonthRate> partialMonthRates = new ArrayList<PartialMonthRate>();
        PartialMonthRate partialMonthRate;
        for(int i=1; i<13; i++) {
        	partialMonthRate = new PartialMonthRate(partialYearRate, i, (float) 0.5);
            partialMonthRates.add(partialMonthRate);
        }
        partialYearRate.setPartialMonthRates(partialMonthRates);
        partialYearRate = partialYearRateRepository.save(partialYearRate);

        partialYearRates = partialYearRateRepository.findAll();
        partialYearRate = partialYearRates.iterator().next();
        org.junit.Assert.assertEquals(partialYearRate.getPropotional(), false);
        org.junit.Assert.assertEquals(partialYearRate.getPartialMonthRates().size(), 12);
        org.junit.Assert.assertEquals(partialYearRate.getPartialMonthRates().get(0).getMonth(), 1);
        org.junit.Assert.assertEquals(partialYearRate.getPartialMonthRates().get(11).getMonth(), 12);
        org.junit.Assert.assertEquals(partialYearRate.getPartialMonthRates().get(11).getRate(), 0.5, 0.01);
    }
    
    @Test
    public void timeoffSetting() {
        TimeoffSetting timeoffSetting = new TimeoffSetting(organizationTop);
        timeoffSetting = timeoffSettingRepository.save(timeoffSetting);
        Iterable<TimeoffSetting> timeoffSettings = timeoffSettingRepository.findAll();
        timeoffSetting = timeoffSettings.iterator().next();
    }

    @Test
    public void AllowancePolicy() {
        TimeoffSetting timeoffSetting = new TimeoffSetting(organizationTop);
        timeoffSetting = timeoffSettingRepository.save(timeoffSetting);

    	AllowancePolicy allowancePolicy = new AllowancePolicy(timeoffSetting,
    														  1, 1, 10);
    	allowancePolicy = allowancePolicyRepository.save(allowancePolicy);
    	Iterable<AllowancePolicy> allowancePolicies = allowancePolicyRepository.findAll();
    	allowancePolicy = allowancePolicies.iterator().next();
        org.junit.Assert.assertEquals(1, allowancePolicy.getAccrual().intValue());
        org.junit.Assert.assertEquals(1, allowancePolicy.getAccrualCycle().intValue());
        org.junit.Assert.assertEquals(10, allowancePolicy.getMax().intValue());
    }

    @Test
    public void TimeoffPolicy() {
        TimeoffSetting timeoffSetting = new TimeoffSetting(organizationTop);
        timeoffSetting = timeoffSettingRepository.save(timeoffSetting);

    	TimeoffPolicy timeoffPolicy = new TimeoffPolicy(timeoffSetting,
                                1, 1, 1, 1, 10, true, true, false, true);
    	timeoffPolicy = timeoffPolicyRepository.save(timeoffPolicy);
    	Iterable<TimeoffPolicy> timeoffPolicies = timeoffPolicyRepository.findAll();
    	timeoffPolicy = timeoffPolicies.iterator().next();
        org.junit.Assert.assertEquals(1, timeoffPolicy.getAccuralBy().intValue());
        org.junit.Assert.assertEquals(1, timeoffPolicy.getAccuralInterval().intValue());
        org.junit.Assert.assertEquals(10, timeoffPolicy.getAccuralLimit().intValue());
        org.junit.Assert.assertEquals(1, timeoffPolicy.getCarryOver().intValue());
        org.junit.Assert.assertEquals(1, timeoffPolicy.getRenewal().intValue());
    }

    @Test
    public void chainSetting() throws NoValueSetError {
        Organization organizationIt = new Organization(cooperation, "IT");
        organizationIt.setUpperLevel(organizationTop);
        organizationIt = organizationRepository.save(organizationIt);

        Organization organizationHr = new Organization(cooperation, "HR");
        organizationHr.setUpperLevel(organizationTop);
        organizationHr = organizationRepository.save(organizationHr);

        Organization organizationUnix = new Organization(cooperation, "UNIX");
        organizationUnix.setUpperLevel(organizationIt);
        organizationUnix = organizationRepository.save(organizationUnix);

        TimeoffSetting timeoffSetting = new TimeoffSetting(organizationTop);
        timeoffSetting = timeoffSettingRepository.save(timeoffSetting);

    	TimeoffPolicy timeoffPolicy = new TimeoffPolicy(timeoffSetting,
                                1, 1, 1, 1, 10, true, true, false, true);
    	timeoffPolicy = timeoffPolicyRepository.save(timeoffPolicy);
    	timeoffSetting.setTimeoffPolicy(timeoffPolicy);

    	AllowancePolicy allowancePolicy = new AllowancePolicy(timeoffSetting,
    														  1, 1, 10);
    	allowancePolicy = allowancePolicyRepository.save(allowancePolicy);
    	timeoffSetting.setAllowancePolicy(allowancePolicy);
    	
    	timeoffSetting = timeoffSettingRepository.save(timeoffSetting);
    	
    	entityManager.refresh(organizationTop);
    	entityManager.refresh(organizationUnix);
    	entityManager.flush();
    	
        org.junit.Assert.assertNull(organizationUnix.getTimeoffSetting());
        org.junit.Assert.assertEquals(organizationUnix.getTimeoffSettingRecursive().getId(),
        							  timeoffSetting.getId());
    }

    @Test
    public void chainSetting1() throws NoValueSetError {
        Organization organizationIt = new Organization(cooperation, "IT");
        organizationIt.setUpperLevel(organizationTop);
        organizationIt = organizationRepository.save(organizationIt);

        Organization organizationHr = new Organization(cooperation, "HR");
        organizationHr.setUpperLevel(organizationTop);
        organizationHr = organizationRepository.save(organizationHr);

        Organization organizationUnix = new Organization(cooperation, "UNIX");
        organizationUnix.setUpperLevel(organizationIt);
        organizationUnix = organizationRepository.save(organizationUnix);

        TimeoffSetting timeoffSetting = new TimeoffSetting(organizationTop);
        timeoffSetting = timeoffSettingRepository.save(timeoffSetting);

    	TimeoffPolicy timeoffPolicy = new TimeoffPolicy(timeoffSetting,
                                1, 1, 1, 1, 10, true, true, false, true);
    	timeoffPolicy = timeoffPolicyRepository.save(timeoffPolicy);
    	timeoffSetting.setTimeoffPolicy(timeoffPolicy);

    	AllowancePolicy allowancePolicy = new AllowancePolicy(timeoffSetting,
    														  1, 1, 10);
    	allowancePolicy = allowancePolicyRepository.save(allowancePolicy);
    	timeoffSetting.setAllowancePolicy(allowancePolicy);
    	
        PartialYearRate partialYearRate = new PartialYearRate(timeoffSetting, true);
        partialYearRate = partialYearRateRepository.save(partialYearRate);
        timeoffSetting.setPartialYearRate(partialYearRate);

    	timeoffSetting = timeoffSettingRepository.save(timeoffSetting);

        TimeoffSetting timeoffSettingUnix = new TimeoffSetting(organizationUnix);
        timeoffSettingUnix = timeoffSettingRepository.save(timeoffSettingUnix);

    	TimeoffPolicy timeoffPolicyUnix = new TimeoffPolicy(timeoffSettingUnix,
                                2, 2, 2, 2, 20, true, true, false, true);
    	timeoffPolicy = timeoffPolicyRepository.save(timeoffPolicyUnix);
    	timeoffSettingUnix.setTimeoffPolicy(timeoffPolicyUnix);

        TimeoffSetting timeoffSettingIt = new TimeoffSetting(organizationIt);
        timeoffSettingIt = timeoffSettingRepository.save(timeoffSettingIt);
    	AllowancePolicy allowancePolicyIt = new AllowancePolicy(timeoffSettingIt,
    														    3, 3, 30);
    	allowancePolicyIt = allowancePolicyRepository.save(allowancePolicyIt);
    	timeoffSettingIt.setAllowancePolicy(allowancePolicyIt);
    	timeoffSettingIt = timeoffSettingRepository.save(timeoffSettingIt);
    	
    	entityManager.flush();
    	entityManager.refresh(organizationTop);
    	entityManager.refresh(organizationUnix);
    	entityManager.refresh(organizationIt);
    	entityManager.refresh(timeoffSetting);
    	entityManager.refresh(timeoffSettingIt);
    	entityManager.refresh(timeoffSettingUnix);

    	timeoffSettingUnix = organizationUnix.getTimeoffSetting();
        org.junit.Assert.assertEquals(timeoffSettingUnix.getId(), timeoffSettingUnix.getId());
        org.junit.Assert.assertEquals(timeoffSettingUnix.getAccuralBy(), 2);
        org.junit.Assert.assertEquals(timeoffSettingUnix.getAccuralInterval(), 2);
        org.junit.Assert.assertEquals(timeoffSettingUnix.getAccuralLimit(), 20);
        org.junit.Assert.assertEquals(timeoffSettingUnix.getCarryOver(), 2);
        org.junit.Assert.assertEquals(timeoffSettingUnix.getCutoffMonth().intValue(), 1);
        org.junit.Assert.assertEquals(timeoffSettingUnix.getCutoffDay().intValue(), 1);
        org.junit.Assert.assertEquals(timeoffSettingUnix.getRenewal(), 2);
        org.junit.Assert.assertEquals(timeoffSettingUnix.getAllowanceAccrual(), 3);
        org.junit.Assert.assertEquals(timeoffSettingUnix.getAllowanceAccrualCycle(), 3);
        org.junit.Assert.assertEquals(timeoffSettingUnix.getAllowanceMax(), 30);
    }
}
