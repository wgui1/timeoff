package cn.timeoff.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
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

import cn.timeoff.core.NoValueSetError;

@Entity
public class TimeoffSetting {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name="cooperation_id")
    private Organization organization;

    @OneToMany(mappedBy="timeoffSetting")
    private Collection<TimeoffPolicy> timeoffPolicies = new ArrayList<TimeoffPolicy>();

    @OneToOne
    @JoinTable( name="setting_timeoffpolicy",
                joinColumns = @JoinColumn(name = "timeoff_setting_id"),
                inverseJoinColumns = @JoinColumn(name = "timeoff_policy_id")
    )
    private TimeoffPolicy timeoffPolicy;

	@OneToMany(mappedBy="timeoffSetting")
    private Collection<AllowancePolicy> allowancePolicies = new ArrayList<AllowancePolicy>();

    @OneToOne
    @JoinTable( name="setting_allowancepolicy",
                joinColumns = @JoinColumn(name = "timeoff_setting_id"),
                inverseJoinColumns = @JoinColumn(name = "allowance_policy_id")
    )
    private AllowancePolicy allowancePolicy;

    @OneToMany(mappedBy="timeoffSetting")
    private Collection<PartialYearRate> partialYearRates = new ArrayList<PartialYearRate>();

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

    public Collection<TimeoffPolicy> getTimeoffPolicies() {
        return timeoffPolicies;
    }

    public void setTimeoffPolicies(Collection<TimeoffPolicy> timeoffPolicies) {
        this.timeoffPolicies = timeoffPolicies;
    }

    public Collection<AllowancePolicy> getAllowancePolicies() {
        return allowancePolicies;
    }

    public void setAllowancePolicies(Collection<AllowancePolicy> allowancePolicies) {
        this.allowancePolicies = allowancePolicies;
    }
    
    public Collection<PartialYearRate> getPartialYearRates() {
        return partialYearRates;
    }

    public void setPartialYearRates(Collection<PartialYearRate> partialYearRates) {
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
    
    public boolean isActive() throws NoValueSetError{
    	Boolean isActive = null;
    	if (timeoffPolicy != null) {
    		isActive = timeoffPolicy.isActive();
    	}
        if (isActive == null) {
            Organization upperLevel = organization.getUpperLevel();
            if (upperLevel != null) {
                isActive = upperLevel.getTimeoffSetting().isActive();
            }
            if ( isActive == null) {
                throw new NoValueSetError("Value 'isActive' is not set");
            }
        }
        return isActive;
	}

	public int getRenewal() throws NoValueSetError{
    	Integer renewal = null;
    	if (timeoffPolicy != null) {
    		renewal = timeoffPolicy.getRenewal();
    	}
        if (renewal == null) {
            Organization upperLevel = organization.getUpperLevel();
            if (upperLevel != null) {
                renewal = upperLevel.getTimeoffSetting().getRenewal();
            }
            if ( renewal == null) {
                throw new NoValueSetError("Value 'renewal' is not set");
            }
        }
        return renewal;
	}

	public int getCarryOver()  throws NoValueSetError{
    	Integer carryOver = null;
    	if (timeoffPolicy != null) {
    		carryOver = timeoffPolicy.getCarryOver();
    	}
        if (carryOver == null) {
            Organization upperLevel = organization.getUpperLevel();
            if (upperLevel != null) {
                carryOver = upperLevel.getTimeoffSetting().getCarryOver();
            }
            if ( carryOver == null) {
                throw new NoValueSetError("Value 'carryOver' is not set");
            }
        }
        return carryOver;
	}

	public int getAccuralInterval()  throws NoValueSetError{
    	Integer accuralInterval = null;
    	if (timeoffPolicy != null) {
    		accuralInterval = timeoffPolicy.getAccuralInterval();
    	}
        if (accuralInterval == null) {
            Organization upperLevel = organization.getUpperLevel();
            if (upperLevel != null) {
                accuralInterval = upperLevel.getTimeoffSetting().getAccuralInterval();
            }
            if ( accuralInterval == null) {
                throw new NoValueSetError("Value 'accuralInterval' is not set");
            }
        }
        return accuralInterval;
	}

	public int getAccuralBy()  throws NoValueSetError{
    	Integer accuralBy = null;
    	if (timeoffPolicy != null) {
    		accuralBy = timeoffPolicy.getAccuralBy();
    	}
        if (accuralBy == null) {
            Organization upperLevel = organization.getUpperLevel();
            if (upperLevel != null) {
                accuralBy = upperLevel.getTimeoffSetting().getAccuralBy();
            }
            if ( accuralBy == null) {
                throw new NoValueSetError("Value 'accuralBy' is not set");
            }
        }
        return accuralBy;
	}

	public int getAccuralLimit()  throws NoValueSetError{
    	Integer accuralLimit = null;
    	if (timeoffPolicy != null) {
    		accuralLimit = timeoffPolicy.getAccuralLimit();
    	}
        if (accuralLimit == null) {
            Organization upperLevel = organization.getUpperLevel();
            if (upperLevel != null) {
                accuralLimit = upperLevel.getTimeoffSetting().getAccuralLimit();
            }
            if ( accuralLimit == null) {
                throw new NoValueSetError("Value 'accuralLimit' is not set");
            }
        }
        return accuralLimit;
	}

	public boolean isBalanceRequired()  throws NoValueSetError{
    	Boolean balanceRequired = null;
    	if (timeoffPolicy != null) {
    		balanceRequired = timeoffPolicy.isBalanceRequired();
    	}
        if (balanceRequired == null) {
            Organization upperLevel = organization.getUpperLevel();
            if (upperLevel != null) {
                balanceRequired = upperLevel.getTimeoffSetting().isBalanceRequired();
            }
            if ( balanceRequired == null) {
                throw new NoValueSetError("Value 'balanceRequired' is not set");
            }
        }
        return balanceRequired;
	}

	public boolean isAutoApproval()  throws NoValueSetError{
    	Boolean isAutoApproval = null;
    	if (timeoffPolicy != null) {
    		isAutoApproval = timeoffPolicy.isAutoApproval();
    	}
        if (isAutoApproval == null) {
            Organization upperLevel = organization.getUpperLevel();
            if (upperLevel != null) {
                isAutoApproval = upperLevel.getTimeoffSetting().isAutoApproval();
            }
            if ( isAutoApproval == null) {
                throw new NoValueSetError("Value 'isAutoApproval' is not set");
            }
        }
        return isAutoApproval;
	}

	public boolean isRequestEanbled()  throws NoValueSetError{
    	Boolean isRequestEanbled = null;
    	if (timeoffPolicy != null) {
    		isRequestEanbled = timeoffPolicy.isRequestEanbled();
    	}
        if (isRequestEanbled == null) {
            Organization upperLevel = organization.getUpperLevel();
            if (upperLevel != null) {
                isRequestEanbled = upperLevel.getTimeoffSetting().isRequestEanbled();
            }
            if ( isRequestEanbled == null) {
                throw new NoValueSetError("Value 'isRequestEanbled' is not set");
            }
        }
        return isRequestEanbled;
	}

	public int getAllowanceAccrual()  throws NoValueSetError{
    	Integer accural = null;
    	if (allowancePolicy != null) {
    		accural = allowancePolicy.getAccrual();
    	}
        if (accural == null) {
            Organization upperLevel = organization.getUpperLevel();
            if (upperLevel != null) {
                accural = upperLevel.getTimeoffSetting().getAllowanceAccrual();
            }
            if ( accural == null) {
                throw new NoValueSetError("Value 'accural' is not set");
            }
        }
        return accural;
	}

	public int getAllowanceMax()  throws NoValueSetError{
    	Integer max = null;
    	if (allowancePolicy != null) {
    		max = allowancePolicy.getMax();
    	}
        if (max == null) {
            Organization upperLevel = organization.getUpperLevel();
            if (upperLevel != null) {
                max = upperLevel.getTimeoffSetting().getAllowanceMax();
            }
            if ( max == null) {
                throw new NoValueSetError("Value 'max' is not set");
            }
        }
        return max;
	}

	public int getAllowanceAccrualCycle()  throws NoValueSetError{
    	Integer accuralCycle = null;
    	if (allowancePolicy != null) {
    		accuralCycle = allowancePolicy.getAccrualCycle();
    	}
        if (accuralCycle == null) {
            Organization upperLevel = organization.getUpperLevel();
            if (upperLevel != null) {
                accuralCycle = upperLevel.getTimeoffSetting().getAllowanceAccrualCycle();
            }
            if ( accuralCycle == null) {
                throw new NoValueSetError("Value 'accuralCycle' is not set");
            }
        }
        return accuralCycle;
	}

	public boolean isPropotional()  throws NoValueSetError{
    	Boolean isPropotional = null;
    	if (partialYearRate != null) {
    		isPropotional = partialYearRate.isPropotional();
    	}
        if (isPropotional == null) {
            Organization upperLevel = organization.getUpperLevel();
            if (upperLevel != null) {
                isPropotional = upperLevel.getTimeoffSetting().isPropotional();
            }
            if ( isPropotional == null) {
                throw new NoValueSetError("Value 'isPropotional' is not set");
            }
        }
        return isPropotional;
	}

	public List<PartialMonthRate> getPartialMonthRates()  throws NoValueSetError{
    	List<PartialMonthRate> partialMonthRates = null;
    	if (partialYearRate != null) {
    		partialMonthRates = partialYearRate.getPartialMonthRates();
    	}
        if (partialMonthRates == null) {
            Organization upperLevel = organization.getUpperLevel();
            if (upperLevel != null) {
                partialMonthRates = upperLevel.getTimeoffSetting().getPartialMonthRates();
            }
            if ( partialMonthRates == null) {
                throw new NoValueSetError("Value 'partialMonthRates' is not set");
            }
        }
        return partialMonthRates;
	}
}