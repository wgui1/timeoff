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
public class TimeoffPolicy {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    
    @ManyToOne(optional=false)
    @JoinColumn(name="timeoff_setting_id")
    private TimeoffSetting timeoffSetting;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Timestamp createdDate;

    private Integer renewal;
    private Integer carryOver;
    private Integer accuralInterval;
    private Integer accuralBy;
    private Integer accuralLimit;
    private Boolean active;
    private Boolean balanceRequired;
    private Boolean autoApproval;
    private Boolean requestEanbled;

    public TimeoffPolicy() {
        super();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getRenewal() {
        return renewal;
    }

    public void setRenewal(Integer renewal) {
        this.renewal = renewal;
    }

    public Integer getCarryOver() {
        return carryOver;
    }

    public void setCarryOver(Integer carryOver) {
        this.carryOver = carryOver;
    }

    public Integer getAccuralInterval() {
        return accuralInterval;
    }

    public void setAccuralInterval(Integer accuralInterval) {
        this.accuralInterval = accuralInterval;
    }

    public Integer getAccuralBy() {
        return accuralBy;
    }

    public void setAccuralBy(Integer accuralBy) {
        this.accuralBy = accuralBy;
    }

    public Integer getAccuralLimit() {
        return accuralLimit;
    }

    public void setAccuralLimit(Integer accuralLimit) {
        this.accuralLimit = accuralLimit;
    }

    public Boolean isBalanceRequired() {
        return balanceRequired;
    }

    public void setBalanceRequired(Boolean balanceRequired) {
        this.balanceRequired = balanceRequired;
    }

    public Boolean isAutoApproval() {
        return autoApproval;
    }

    public void setAutoApproval(Boolean autoApproval) {
        this.autoApproval = autoApproval;
    }

    public Boolean isRequestEanbled() {
        return requestEanbled;
    }

    public void setRequestEanbled(Boolean requestEanbled) {
        this.requestEanbled = requestEanbled;
    }

	public TimeoffSetting getTimeoffSetting() {
		return timeoffSetting;
	}

	public void setTimeoffSetting(TimeoffSetting timeoffSetting) {
		this.timeoffSetting = timeoffSetting;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

}