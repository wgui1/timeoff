package cn.timeoff.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;

import cn.timeoff.security.model.Domain;

@Entity
public class AllowancePolicy {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    private int accrual;
    private int accrual_cycle;
    private int max;

    public AllowancePolicy() {
        super();
    }

}