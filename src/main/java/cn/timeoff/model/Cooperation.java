package cn.timeoff.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import cn.timeoff.security.model.Domain;

@Entity
public class Cooperation {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name="domain_id")
    private Domain domain;

    @LastModifiedBy
    private String lastModifiedBy;

    @LastModifiedDate
    private Timestamp lastModifiedTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

}
