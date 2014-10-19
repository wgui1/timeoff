package cn.timeoff.repository;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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
    @Transactional
    public void organization() {
        Organization organization_it = new Organization(cooperation, "IT");
        organization_it.setUpperLevel(organizationTop);
        organization_it = organizationRepository.save(organization_it);

        Organization organization_hr = new Organization(cooperation, "HR");
        organization_hr.setUpperLevel(organizationTop);
        organization_hr = organizationRepository.save(organization_hr);

        Organization organization_unix = new Organization(cooperation, "UNIX");
        organization_unix.setUpperLevel(organization_it);
        organization_unix = organizationRepository.save(organization_unix);
        
        boolean hasIt = false;
        boolean hasHr = false;
        Iterable<Organization> organizations = organizationRepository.findAll();
        for(Organization o: organizations) {
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
    @Transactional
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
    @Transactional
    public void partialYearRateTest() {
        PartialYearRate partialYearRate = new PartialYearRate(true);
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
    @Transactional
    public void timeoffSetting() {
        TimeoffSetting timeoffSetting = new TimeoffSetting(organizationTop);
        timeoffSetting = timeoffSettingRepository.save(timeoffSetting);
        Iterable<TimeoffSetting> timeoffSettings = timeoffSettingRepository.findAll();
        timeoffSetting = timeoffSettings.iterator().next();
    }

    @Test
    @Transactional
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
    @Transactional
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
    @Transactional
    public void chainSetting() {
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
}
