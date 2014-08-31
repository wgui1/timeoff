package cn.timeoff.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import cn.timeoff.security.model.Domain;

@Entity
public class Setting {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name="domain_id")
    private Domain domain;

    private String name;

    @ManyToOne
    @JoinColumn(name="filter_id")
    private Filter filter;

	public Setting() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
