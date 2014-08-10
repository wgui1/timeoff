package cn.timeoff.security.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="groups",
       uniqueConstraints = {
       @UniqueConstraint(columnNames = {"domain_id", "name"} )
} )
public class Group {
	
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name="domain_id")
    private Domain domain;

    @Column(length=100, nullable=false)
    private String name;

    @OneToMany(mappedBy="group", cascade=CascadeType.REMOVE)
    private List<GroupMember> members;

    @OneToMany(mappedBy="group", cascade=CascadeType.REMOVE)
    private List<GroupAuthority> authorities;
    
    public Group() {
	}

    public Group(Domain domain, String name) {
    	this.domain = domain;
    	this.name = name;
	}

    public long getId() {
		return id;
	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<GroupMember> getMembers() {
		return members;
	}

	public void setMembers(List<GroupMember> members) {
		this.members = members;
	}

	public List<GroupAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<GroupAuthority> authorities) {
		this.authorities = authorities;
	}
}
