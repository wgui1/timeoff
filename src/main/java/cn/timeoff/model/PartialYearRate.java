package cn.timeoff.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

@Entity
public class PartialYearRate {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name="timeoff_setting_id")
    private TimeoffSetting timeoffSetting;

    private Boolean propotional;

    private List<Float> ratePerMonth = new ArrayList<Float>();

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Timestamp createdDate;

    public Boolean isPropotional() {
        return propotional;
    }

    public void setPropotional(Boolean propotional) {
        this.propotional = propotional;
    }

    public List<Float> getRatePerMonth() {
        return ratePerMonth;
    }

    public void setRatePerMonth(List<Float> ratePerMonth) {
        this.ratePerMonth = ratePerMonth;
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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