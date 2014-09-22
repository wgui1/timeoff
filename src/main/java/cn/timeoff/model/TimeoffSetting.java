package cn.timeoff.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
public class TimeoffSetting {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name="cooperation_id")
    private Organization organization;

    @OneToMany
    @JoinTable(
            name="setting_policy",
            joinColumns = @JoinColumn(name = "timeoffsetting_id"),
            inverseJoinColumns = @JoinColumn(name = "timeoffpolicy_id")
    )
    private List<TimeoffPolicy> timeoffPolicies = new ArrayList<TimeoffPolicy>();
    
    private TimeoffPolicy timeoffPolicy;
    
    @OneToMany
    @JoinTable(
            name="setting_allowance",
            joinColumns = @JoinColumn(name = "timeoffsetting_id"),
            inverseJoinColumns = @JoinColumn(name = "timeoffallowance_id")
    )
    private List<AllowancePolicy> allowancePolicies;

    @OneToMany
    @JoinTable(
            name="setting_partialyear",
            joinColumns = @JoinColumn(name = "timeoffsetting_id"),
            inverseJoinColumns = @JoinColumn(name = "partialyearrate_id")
    )

    private List<PartialYearRate> partialYearRates;
    
    @LastModifiedBy
    private String lastModifiedBy;

    @LastModifiedDate
    private Timestamp lastModifiedTime;

    private Timestamp cutoffDate;

    private String name;

    public TimeoffSetting() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public List<TimeoffPolicy> getTimeoffPolicies() {
        return timeoffPolicies;
    }

    public void setTimeoffPolicies(List<TimeoffPolicy> timeoffPolicies) {
        this.timeoffPolicies = timeoffPolicies;
    }

    public List<AllowancePolicy> getAllowancePolicies() {
        return allowancePolicies;
    }

    public void setAllowancePolicies(List<AllowancePolicy> allowancePolicies) {
        this.allowancePolicies = allowancePolicies;
    }
    
    public AllowancePolicy getAllowancePolicy getAllowancePolicy() {
    	if (allowancePolicies == null || allowancePolicies.isEmpty()) {
    		return null;
    	}
    	allowancePolicies
    	
    }

    public List<PartialYearRate> getPartialYearRates() {
        return partialYearRates;
    }

    public void setPartialYearRates(List<PartialYearRate> partialYearRates) {
        this.partialYearRates = partialYearRates;
    }

    public Timestamp getCutoffDate() {
        return cutoffDate;
    }

    public void setCutoffDate(Timestamp cutoffDate) {
        this.cutoffDate = cutoffDate;
    }

}