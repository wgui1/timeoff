package cn.timeoff.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class PartialMonthRate {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

	@ManyToOne(optional=false)
	@JoinColumn(name = "partial_year_rate_id", nullable = false)
	private PartialYearRate partialYearRate;

	private int month;
	private float rate;

	public PartialMonthRate() {
		super();
	}

	public PartialMonthRate(PartialYearRate partialYearRate, int month, float rate) {
		super();
		this.partialYearRate = partialYearRate;
		this.month = month;
		this.rate = rate;
	}

	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public float getRate() {
		return rate;
	}
	public void setRate(float rate) {
		this.rate = rate;
	}

	public PartialYearRate getPartialYearRate() {
		return partialYearRate;
	}

	public void setPartialYearRate(PartialYearRate partialYearRate) {
		this.partialYearRate = partialYearRate;
	}

}
