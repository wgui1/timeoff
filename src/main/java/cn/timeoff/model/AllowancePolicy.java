package cn.timeoff.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

@Entity
public class AllowancePolicy {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="timeoff_setting_id")
    private TimeoffSetting timeoffSetting;

    private Integer accrual;
    private Integer accrualCycle;
    private Integer accrualType;
    private Integer max;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Timestamp createdDate;

    public AllowancePolicy() {
        super();
    }

	public AllowancePolicy(TimeoffSetting timeoffSetting, Integer accrual,
			Integer accrualCycle, Integer max) {
		super();
		this.timeoffSetting = timeoffSetting;
		this.accrual = accrual;
		this.accrualCycle = accrualCycle;
		this.max = max;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getAccrual() {
		return accrual;
	}

	public void setAccrual(Integer accrual) {
		this.accrual = accrual;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public Integer getAccrualCycle() {
		return accrualCycle;
	}

	public void setAccrualCycle(Integer accrualCycle) {
		this.accrualCycle = accrualCycle;
	}

	public Integer getAccrualType() {
		return accrualType;
	}

	public void setAccrualType(Integer accrualType) {
		this.accrualType = accrualType;
	}

}