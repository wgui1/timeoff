package cn.timeoff.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;

@Entity
public class TimeoffPolicy {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    private int renewal;
    private int carryover;
    private int accural_internal;
    private int accural_by;
    private int accural_limit;
    private boolean active;
    private boolean balance_required;
    private boolean auto_approval;
    private boolean request_eanbled;

	public TimeoffPolicy() {
		super();
	}

}