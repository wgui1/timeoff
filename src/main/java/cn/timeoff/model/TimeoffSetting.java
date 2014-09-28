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

    @OneToMany(mappedBy="timeoffSetting")
    private List<TimeoffPolicy> timeoffPolicies = new ArrayList<TimeoffPolicy>();

    @OneToOne
    @JoinTable( name="setting_timeoffpolicy",
                joinColumns = @JoinColumn(name = "timeoff_setting_id"),
                inverseJoinColumns = @JoinColumn(name = "timeoff_policy_id")
    )
    private TimeoffPolicy timeoffPolicy;

	@OneToMany(mappedBy="timeoffSetting")
    private List<AllowancePolicy> allowancePolicies = new ArrayList<AllowancePolicy>();

    @OneToOne
    @JoinTable( name="setting_allowancepolicy",
                joinColumns = @JoinColumn(name = "timeoff_setting_id"),
                inverseJoinColumns = @JoinColumn(name = "allowance_policy_id")
    )
    private AllowancePolicy allowancePolicy;

    @OneToMany(mappedBy="timeoffSetting")
    private List<PartialYearRate> partialYearRates = new ArrayList<PartialYearRate>();

    @OneToOne
    @JoinTable( name="setting_partialyearrate",
                joinColumns = @JoinColumn(name = "timeoff_setting_id"),
                inverseJoinColumns = @JoinColumn(name = "partial_year_rate_id")
    )
    private PartialYearRate partialYearRate;
    
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

    public TimeoffPolicy getTimeoffPolicy() {
        return timeoffPolicy;
    }

    public void setTimeoffPolicy(TimeoffPolicy timeoffPolicy) {
        this.timeoffPolicy = timeoffPolicy;
    }

    public AllowancePolicy getAllowancePolicy() {
        return allowancePolicy;
    }

    public void setAllowancePolicy(AllowancePolicy allowancePolicy) {
        this.allowancePolicy = allowancePolicy;
    }

    public PartialYearRate getPartialYearRate() {
        return partialYearRate;
    }

    public void setPartialYearRate(PartialYearRate partialYearRate) {
        this.partialYearRate = partialYearRate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Timestamp getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Timestamp lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }
    
    public boolean isActive() {
    	if (timeoffPolicy != null) {
    		isActive = timeoffPolicy.isActive();
    		if (isActive != null) {
                return isActive;
    		}
    	}
        if (organization.getUpperLevel() != null)
    	}
	}

	public int getRenewal() {
		return timeoffPolicy.getRenewal();
	}

	public int getCarryOver() {
		return timeoffPolicy.getCarryOver();
	}

	public int getAccuralInterval() {
		return timeoffPolicy.getAccuralInterval();
	}

	public int getAccuralBy() {
		return timeoffPolicy.getAccuralBy();
	}

	public int getAccuralLimit() {
		return timeoffPolicy.getAccuralLimit();
	}

	public boolean isBalanceRequired() {
		return timeoffPolicy.isBalanceRequired();
	}

	public boolean isAutoApproval() {
		return timeoffPolicy.isAutoApproval();
	}

	public boolean isRequestEanbled() {
		return timeoffPolicy.isRequestEanbled();
	}

	public int getAccrual() {
		return allowancePolicy.getAccrual();
	}

	public int getMax() {
		return allowancePolicy.getMax();
	}

	public int getAccrualCycle() {
		return allowancePolicy.getAccrualCycle();
	}

	public boolean isPropotional() {
		return partialYearRate.isPropotional();
	}

	public List<Float> getRatePerMonth() {
		return partialYearRate.getRatePerMonth();
	}
}