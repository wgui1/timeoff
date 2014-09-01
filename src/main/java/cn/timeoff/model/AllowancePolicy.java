package cn.timeoff.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import cn.timeoff.security.model.Domain;

@Entity
public class AllowancePolicy {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name="domain_id")
    private Domain domain;
    
    private int accrual;
    private int accrual_cycle;
    private int max;

	public AllowancePolicy() {
		super();
	}

	public Domain getDomain() {
		return domain;
	}

	public void setName(Domain domain) {
		this.domain = domain;
	}

}