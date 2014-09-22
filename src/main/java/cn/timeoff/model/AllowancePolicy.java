package cn.timeoff.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Immutable;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import cn.timeoff.security.model.Domain;

@Entity
@Immutable
public class AllowancePolicy {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    private int accrual;
    private int accrualCycle;
    private int max;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Timestamp createdDate;

    public AllowancePolicy() {
        super();
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getAccrual() {
		return accrual;
	}

	public void setAccrual(int accrual) {
		this.accrual = accrual;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getAccrualCycle() {
		return accrualCycle;
	}

	public void setAccrualCycle(int accrualCycle) {
		this.accrualCycle = accrualCycle;
	}

}