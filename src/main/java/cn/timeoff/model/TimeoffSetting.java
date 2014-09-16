package cn.timeoff.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

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
    private List<TimeoffPolicy> timeoff_policies;

    @OneToMany
    @JoinTable(
            name="setting_allowance",
            joinColumns = @JoinColumn(name = "timeoffsetting_id"),
            inverseJoinColumns = @JoinColumn(name = "timeoffallowance_id")
    )
    private List<AllowancePolicy> allowance_policies;

    @OneToMany
    @JoinTable(
            name="setting_date",
            joinColumns = @JoinColumn(name = "timeoffsetting_id"),
            inverseJoinColumns = @JoinColumn(name = "datecalculation_id")
    )
    private List<DateCalculation> date_calculations;

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
}